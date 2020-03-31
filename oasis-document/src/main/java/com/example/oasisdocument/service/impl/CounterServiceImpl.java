package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.repository.docs.PaperRepository;
import com.example.oasisdocument.service.BaseService;
import com.example.oasisdocument.service.CounterService;
import com.example.oasisdocument.utils.ComputeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CounterServiceImpl implements CounterService {
	private static final Logger logger = LoggerFactory.getLogger(CounterService.class);
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ComputeUtil computeUtil;
	@Autowired
	private PaperRepository paperRepository;
	@Autowired
	private BaseService baseService;

	@Override
	public CounterBaseEntity getSummaryInfo(String id) {
		CounterBaseEntity en = mongoTemplate.findOne(Query.query(new Criteria("checkId").is(id)
				.and("year").is(-1)), CounterBaseEntity.class);
		if (null == en)
			throw new EntityNotFoundException();
		return en;
	}

	/**
	 * 基本计数信息初始化,不包含年份的分析
	 */
	@Override
	@Async
	public void initCounterPOJOSummary() {
		new Thread(()->{

			//作者
			for (Author entity : mongoTemplate.findAll(Author.class)) {
				List<Paper> papers = baseService.getPapersByAuthorName(entity.getAuthorName());
				persistCounterSummary(entity.getId(), papers);
			}
		}).start();
		new Thread(()->{

			//机构
			for (Affiliation entity : mongoTemplate.findAll(Affiliation.class)) {
				//获取全部author
				try {
					List<Paper> papers = mongoTemplate.find(Query.query(new Criteria("affiliations")
							.regex(entity.getAffiliationName())), Paper.class);
					persistCounterSummary(entity.getId(), papers);
				} catch (Exception ignore) {
				}
			}
		}).start();
		new Thread(()->{

			//会议
			for (Conference entity : mongoTemplate.findAll(Conference.class)) {
				List<Paper> papers = baseService.getPapersByConferenceName(entity.getConferenceName());
				persistCounterSummary(entity.getId(), papers);
			}
		}).start();
		new Thread(()->{

			//领域
			for (Field entity : mongoTemplate.findAll(Field.class)) {
				List<Paper> papers = mongoTemplate.find(Query.query(new Criteria("terms")
						.regex(entity.getFieldName())), Paper.class);
				persistCounterSummary(entity.getId(), papers);
			}
		}).start();
	}


	/**
	 * 持久化单个实体
	 * 1. 首先进行实体的summary搭建 , 每一个记录的生命为 1h
	 * 如果实体已经存在(year = -1) , 并且没有cache超时 , 那么不进行第一步的统计
	 * <p>
	 * 2. 进行每一年的单个年查询 , 每一个记录的生命为 1h
	 * 如果当前年的记录已经存在 , 并且没有超时 , 那么不进行更新
	 */
	public void persistCounterSummary(String id, List<Paper> papers) {
		//save to basic summary count data
		countSingleEntity(id, -1, papers);
	}

	/**
	 * 按照年份持久化counter POJO
	 */
	@Override
	public void initCounterPOJO(String id) {
		List<CounterBaseEntity> entities = mongoTemplate.find(
				new Query(new Criteria("checkId").is(id).and("year").is(-1)), CounterBaseEntity.class);
		if (entities.isEmpty()) throw new BadReqException();
		CounterBaseEntity en = entities.get(0);
		List<String> paperIds = en.getPaperList();
		List<Paper> papers = new LinkedList<>();
		paperIds.forEach((String pId) -> {
			List<Paper> ps = paperRepository.findAllById(pId);
			papers.addAll(ps);
		});
		Map<Integer, List<Paper>> hash = new HashMap<>();
		for (Paper paper : papers) {
			int year = paper.getYear();
			List<Paper> tmpList = hash.getOrDefault(year, new LinkedList<>());
			tmpList.add(paper);
			hash.put(year, tmpList);
		}
		for (int y : hash.keySet()) {
			countSingleEntity(id, y, hash.get(y));
		}
	}

	/**
	 * 对单个年份进行持久化
	 *
	 * @param id:     最终的checkId
	 * @param year:   年份,如果是-1那么就是对全部年份的统计
	 * @param papers: 当年相关paper
	 */
	public void countSingleEntity(String id, int year, List<Paper> papers) {
		if (mongoTemplate.exists(Query.query(new Criteria("checkId").is(id)
						.and("year").is(year)),
				CounterBaseEntity.class)) {
			return;
		}

		CounterBaseEntity totalPOJO = new CounterBaseEntity();
		totalPOJO.setCheckId(id);
		totalPOJO.setYear(year);        // year < 0 denotes the total
		totalPOJO.setActiveness(computeUtil.getActiveness(papers));
		//设置所有的论文id
		totalPOJO.setPaperList(papers.stream().map(Paper::getId).collect(Collectors.toList()));
		totalPOJO.setPaperCount(computeUtil.getPaperCount(papers));
		totalPOJO.setCitationCount(computeUtil.getCitationCount(papers));
		totalPOJO.setH_index(computeUtil.getH_index(papers));
		totalPOJO.setHeat(computeUtil.getHeat(papers));
		//save
		mongoTemplate.save(totalPOJO);
	}
}
