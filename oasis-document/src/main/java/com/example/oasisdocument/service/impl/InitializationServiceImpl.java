package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.InitializationService;
import com.example.oasisdocument.utils.ComputeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InitializationServiceImpl implements InitializationService {
	private static final Logger logger = LoggerFactory.getLogger(InitializationService.class);
	private static final char BLANK = ' ';
	private static final char FIELD_SPLITTER = ',';
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ComputeUtil computeUtil;

	@Override
	public CounterBaseEntity getSummaryInfo(String id) {
		List<CounterBaseEntity> en = mongoTemplate.find(Query.query(new Criteria("checkId").is(id)
				.and("year").is(-1)), CounterBaseEntity.class);
		if (en.isEmpty()) throw new EntityNotFoundException();
		return en.get(0);
	}

	//机构初始化
	//会议初始化
	@Async
	@Override
	public void initAffiliationBase() {
		final String checkColumn = "affiliationName";
		Set<String> affiliationNameFullSet = mongoTemplate.findAll(Author.class)
				.stream()
				.map(Author::getAffiliationName)
				.filter((String name) -> !name.isEmpty())
				.collect(Collectors.toSet());
		for (String affName : affiliationNameFullSet) {
			//不添加重复项
			if (mongoTemplate.exists(
					Query.query(Criteria.where(checkColumn).is(affName)), Affiliation.class)) {
				continue;
			}
			Affiliation entity = new Affiliation();
			entity.setAffiliationName(affName);
			//存储
			entity = mongoTemplate.save(entity);
		}
	}

	//领域信息添加
	@Async
	@Override
	public void initConferenceBasic() {
		final String checkColumn = "conferenceName";
		Set<String> conferenceRawSet = mongoTemplate.findAll(Paper.class)
				.stream()
				.map(Paper::getConference)
				.collect(Collectors.toSet());
		Set<String> conSet = new HashSet<>();
		final String rankerPattern = "[0-9]+[a-z]+$";
		for (String conferenceName : conferenceRawSet) {
			Conference conference = new Conference();
			if (mongoTemplate.exists(Query.query(Criteria.where(checkColumn).is(conferenceName)),
					Conference.class)) {
				continue;
			}
			//2015 IEEE/ACM 37th IEEE International Conference on Software Engineering
//			String[] words = conferenceName.split(String.valueOf(BLANK));
//			StringBuilder sb = new StringBuilder();
//			int year = -1;
//			String ranker = "";
//			for (String word : words) {
//				if (isNumeric(word)) {
//					try {
//						year = Integer.parseInt(word.substring(0, 4));
//					} catch (Exception ignored) {
//					}
//				} else if (word.matches(rankerPattern)) {
//					ranker = word;
//				} else sb.append(BLANK).append(word);
//			}
//			conference.setConferenceName(sb.toString().trim());
			conference.setConferenceName(conferenceName);
//			conference.setRanker(ranker);
//			conference.setYear(year);
			mongoTemplate.save(conference);
		}

	}

	@Async
	@Override
	public void initFieldBasic() {
		final String checkColumn = "fieldName";
		List<Author> authorList = mongoTemplate.findAll(Author.class);
		Set<String> filedSet = new HashSet<>();
		for (Author author : authorList) {
			String[] fields = author.getField().split(String.valueOf(FIELD_SPLITTER));
			filedSet.addAll(Arrays.asList(fields));
		}
		filedSet.stream()
				.filter((String name) -> !name.isEmpty())
				.forEach((String name) -> {
					if (!mongoTemplate.exists(Query.query(Criteria.where(checkColumn).is(name)), Field.class)) {
						Field field = new Field();
						field.setFieldName(name);
						mongoTemplate.save(field);
					}
				});
	}

	/**
	 * 基本计数信息初始化,不包含年份的分析
	 */
	@Override
	public void initCounterPOJOSummary() {
		final String authorCol = "authors";
		final String conferenceCol = "conference";
		final String termCol = "terms";
		final String affiliationCol = "affiliations";
		new Thread(() -> {
			//作者
			for (Author entity : mongoTemplate.findAll(Author.class)) {
				List<Paper> papers = mongoTemplate.find(
						Query.query(Criteria.where(authorCol).regex(entity.getAuthorName())),
						Paper.class);
				String id = entity.getId();
				persistCounterSummary(id, papers);
			}
		}).start();

//		new Thread(() -> {
//			//机构
//			for (Affiliation entity : mongoTemplate.findAll(Affiliation.class)) {
//				List<Paper> papers = mongoTemplate.find(
//						Query.query(Criteria.where(affiliationCol).regex(entity.getAffiliationName())),
//						Paper.class);
//				String id = entity.getId();
//				persistCounterSummary(id, papers);
//
//			}
//		}).start();
//		new Thread(() -> {
//			//会议
//			for (Conference entity : mongoTemplate.findAll(Conference.class)) {
//				List<Paper> papers = mongoTemplate.find(
//						Query.query(Criteria.where(conferenceCol).is(entity.getConferenceName())),
//						Paper.class);
//				String id = entity.getId();
//				persistCounterSummary(id, papers);
//			}
//		}).start();

		new Thread(() -> {
//			//领域
			for (Field entity : mongoTemplate.findAll(Field.class)) {
				List<Paper> papers = mongoTemplate.find(
						Query.query(Criteria.where(termCol).regex(entity.getFieldName())),
						Paper.class);
				String id = entity.getId();
				persistCounterSummary(id, papers);
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
	private void persistCounterSummary(String id, List<Paper> papers) {
		//save to basic summary count data
		countSingleEntity(id, -1, papers);
	}

	/**
	 * 补充年份数据
	 */
	private void persistCounterYear(String id, List<Paper> papers) {
		for (Integer y : papers.stream().map(Paper::getYear).collect(Collectors.toSet()))
			countSingleEntity(id, y, papers);
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

	private static boolean isNumeric(String str) {
		String bigStr;
		try {
			bigStr = new BigDecimal(str).toString();
		} catch (Exception e) {
			return false;//异常 说明包含非数字。
		}
		return true;
	}
}
