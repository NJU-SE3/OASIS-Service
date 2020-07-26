package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.CounterService;
import com.example.oasisdocument.service.FieldService;
import com.example.oasisdocument.service.IntermeService;
import com.example.oasisdocument.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.oasisdocument.service.IntermeService.affCounterCollection;
import static com.example.oasisdocument.service.IntermeService.fieldCounterCollection;


@Service
public class FieldServiceImpl implements FieldService {
	private static final String refineSplitter = ":";
	private static final String fieldSpilitter = ",";
	private static final String authorNameSplitter = ";";
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CounterService counterService;
	@Autowired
	private IntermeService intermeService;
	@Autowired
	private GeneralJsonVO generalJsonVO;
	@Autowired
	private PageHelper pageHelper;

	@Override
	@Cacheable(cacheNames = "fetchEnById", unless = "#result==null")
	public Field fetchEnById(String id) {
		Field en = mongoTemplate.findById(id, Field.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}

	@Override
	@Cacheable(cacheNames = "fetchFieldList", unless = "#result==null")
	public JSONArray fetchFieldList(String refinement, int pageNum, int pageSize) {
		//应当包含会议id
		final String conKey = "conference", affKey = "affiliation";
		String[] datas = refinement.split(refineSplitter);
		if (datas.length != 2)
			throw new BadReqException();
		String id = datas[1];
		Set<Field> fieldSet;
		if (datas[0].equals(conKey) || datas[0].equals(affKey)) {
			//找到全部关联的作者
			CounterBaseEntity en =
					counterService.getSummaryInfo(id);
			if (null == en)
				throw new EntityNotFoundException();
			List<Paper> papers = en.getPaperList().stream()
					.map((String pid) -> mongoTemplate.findById(pid, Paper.class)).collect(Collectors.toList());
			fieldSet = new HashSet<>();
			for (Paper paper : papers) {
				Set<String> fieldIdSet = new HashSet<>();
				for (String name : Paper.getAllTerms(paper)) {
					Field field = mongoTemplate.findOne(Query.query(new Criteria("fieldName").is(name)),
							Field.class);
					if (field != null && !fieldIdSet.contains(field.getId())) {
						fieldSet.add(field);
						fieldIdSet.add(field.getId());
					}
				}
			}
			//return ans;
		} else
			throw new BadReqException();

		JSONArray array = new JSONArray();
		for (Field field : fieldSet) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(field.getId());
			array.add(generalJsonVO.field2VO(field, baseEntity));
		}
		return (JSONArray) pageHelper.of(array, pageSize, pageNum);
	}

	@Override
	public JSONObject fetchFieldList(int pageNum, int pageSize, String rankKey) {
		//一级缓存命中
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		JSONArray data = new JSONArray();
		long itemCnt = mongoTemplate.count(new Query(), Field.class);
		Sort sort = Sort.by(Sort.Direction.DESC, rankKey);
		if (intermeService.intermeExist(fieldCounterCollection)) {
			List<CounterBaseEntity> bufferList = mongoTemplate.find(new Query()
							.with(sort)
							.with(pageable),
					CounterBaseEntity.class, fieldCounterCollection);
			for (CounterBaseEntity entity : bufferList) {
				data.add(fieldBuffer2VO(entity));
			}
		} else {
			//缓存未命中 , 随机返回
			//异步缓存
			intermeService.genFiledCounter();
			//随机返回pageable条目
			List<Field> entities = mongoTemplate.find(new Query()
					.with(pageable).with(sort), Field.class);
			for (Field en : entities) {
				CounterBaseEntity baseEntity = counterService.getSummaryInfo(en.getId());
				data.add(generalJsonVO.field2VO(en, baseEntity));
			}
		}
		JSONObject ans = new JSONObject();
		ans.put("data", data);
		ans.put("itemCnt", itemCnt);
		return ans;
	}

	private JSONObject fieldBuffer2VO(CounterBaseEntity entity) {
		String uid = entity.getCheckId();
		Field field = mongoTemplate.findById(uid, Field.class);
		if (null == field) return new JSONObject();

		return generalJsonVO.field2VO(field, entity);
	}

	@Override
	@Cacheable(cacheNames = "fetchFieldList", unless = "#result==null")
	public void insertFields(Paper entity) {
		final String fieldCol = "fieldName";
		for (String fieldName : Paper.getAllTerms(entity)) {
			Field field = mongoTemplate.findOne(Query.query(new Criteria(fieldCol).is(fieldName)),
					Field.class);
			if (null == field) {
				field = new Field();
				field.setFieldName(fieldName);
				mongoTemplate.save(field);
			}
		}
	}

	@Override
	@Cacheable(cacheNames = "fetchFieldDistribution", unless = "#result==null")
	public JSONArray fetchFieldDistribution(String id) {
		CounterBaseEntity en = counterService.getSummaryInfo(id);
		List<Paper> papers = en.getPaperList()
				.stream()
				.map((String pid) -> mongoTemplate.findById(pid, Paper.class))
				.collect(Collectors.toList());
		Map<String, Integer> hash = new HashMap<>();
		for (Paper paper : papers) {
			for (String term : Paper.getAllTerms(paper)) {
				hash.put(term, hash.getOrDefault(term, 0) + 1);
			}
		}
		JSONArray ans = new JSONArray();
		for (String key : hash.keySet()) {
			JSONObject obj = new JSONObject();
			obj.put("fieldName", key);
			obj.put("count",hash.getOrDefault(key,0));
			ans.add(obj);
		}
		return ans;
	}

}
