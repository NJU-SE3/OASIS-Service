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
import com.example.oasisdocument.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
	private GeneralJsonVO generalJsonVO;
	@Autowired
	private PageHelper pageHelper;

	@Override
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
		List<Field> fieldList;
		if (datas[0].equals(conKey) || datas[0].equals(affKey)) {
			//找到全部关联的作者
			CounterBaseEntity en =
					counterService.getSummaryInfo(id);
			if (null == en)
				throw new EntityNotFoundException();
			List<Paper> papers = en.getPaperList().stream()
					.map((String pid) -> mongoTemplate.findById(pid, Paper.class)).collect(Collectors.toList());
			fieldList = new LinkedList<>();
			for (Paper paper : papers) {
				for (String name : Paper.getAllTerms(paper)) {
					Field field = mongoTemplate.findOne(Query.query(new Criteria("fieldName").is(name)),
							Field.class);
					if (field != null) fieldList.add(field);
				}
			}
			//return ans;
		} else
			throw new BadReqException();

		JSONArray array = new JSONArray();
		for (Field field : fieldList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(field.getId());
			array.add(generalJsonVO.field2VO(field, baseEntity));
		}
		return (JSONArray) pageHelper.of(array, pageSize, pageNum);
	}

	@Override
	@Cacheable(cacheNames = "fetchFieldList", unless = "#result==null")
	public JSONArray fetchFieldList(int pageNum, int pageSize) {
		List<Field> fieldList = mongoTemplate.findAll(Field.class);
		JSONArray array = new JSONArray();
		for (Field field : fieldList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(field.getId());
			array.add(generalJsonVO.field2VO(field, baseEntity));
		}
		return (JSONArray) pageHelper.of(array, pageSize, pageNum);
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
