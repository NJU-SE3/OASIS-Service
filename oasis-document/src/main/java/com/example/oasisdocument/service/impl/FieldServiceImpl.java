package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.FieldService;
import com.example.oasisdocument.service.InitializationService;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private InitializationService initializationService;

	@Override
	public Field fetchEnById(String id) {
		Field en = mongoTemplate.findById(id, Field.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}

	@Override
	public List<Field> fetchFieldList(String refinement) {
		//应当包含会议id
		final String conKey = "conference";
		String[] datas = refinement.split(refineSplitter);
		if (datas.length != 2 || !datas[0].equals(conKey)) throw new BadReqException();
		String conferenceId = datas[1];
		//找到全部关联的作者
		CounterBaseEntity en = initializationService.getSummaryInfo(conferenceId);
		if (null == en) throw new EntityNotFoundException();
		List<Paper> papers = en.getPaperList().stream()
				.map((String pid) -> mongoTemplate.findById(pid, Paper.class)).collect(Collectors.toList());
		List<Field> ans = new LinkedList<>();
		for (Paper paper : papers) {
			List<Author> authorList = paper.getAuthorList();
			for (Author author : authorList) {
				String[] fieldNames = author.getField().split(fieldSpilitter);
				for (String fieldName : fieldNames) {
					Field field = mongoTemplate.findOne(Query.query(new Criteria("fieldName").is(fieldName)),
							Field.class);
					ans.add(field);
				}
			}
		}
		return ans;
	}

	@Override
	public List<Field> fetchFieldList(int pageNum, int pageSize) {
		return mongoTemplate.find(new Query().with(PageRequest.of(pageNum, pageSize)), Field.class);
	}

}
