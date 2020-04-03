package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.CounterService;
import com.example.oasisdocument.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
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

	@Override
	public Field fetchEnById(String id) {
		Field en = mongoTemplate.findById(id, Field.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}

	@Override
	@Cacheable(cacheNames = "fetchFieldList", unless = "#result==null")
	public List<Field> fetchFieldList(String refinement) {
		//应当包含会议id
		final String conKey = "conference";
		String[] datas = refinement.split(refineSplitter);
		if (datas.length != 2 || !datas[0].equals(conKey)) throw new BadReqException();
		String conferenceId = datas[1];
		//找到全部关联的作者
		CounterBaseEntity en = counterService.getSummaryInfo(conferenceId);
		if (null == en) throw new EntityNotFoundException();
		List<Paper> papers = en.getPaperList().stream()
				.map((String pid) -> mongoTemplate.findById(pid, Paper.class)).collect(Collectors.toList());
		List<Field> ans = new LinkedList<>();
		for (Paper paper : papers) {
			for (String name : Paper.getAllTerms(paper)) {
				Field field = mongoTemplate.findOne(Query.query(new Criteria("fieldName").is(name)),
						Field.class);
				if (field != null) ans.add(field);
			}
		}
		return ans;
	}

	@Override
	@Cacheable(cacheNames = "fetchFieldList", unless = "#result==null")
	public List<Field> fetchFieldList(int pageNum, int pageSize) {
		return mongoTemplate.find(new Query().with(PageRequest.of(pageNum, pageSize)), Field.class);
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

}
