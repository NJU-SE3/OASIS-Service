package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


@Service
public class FieldServiceImpl implements FieldService {
	private static final String refineSplitter = ":";
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Field fetchEnById(String id) {
		Field en = mongoTemplate.findById(id, Field.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}

	@Override
	public List<Field> fetchFieldList(String refinement) {
		//应当包含会议id
		final String conKey = "conference", conColName = "affiliationName";
		String[] datas = refinement.split(refineSplitter);
		if (datas.length != 2 || !datas[0].equals(conKey)) throw new BadReqException();
		String conferenceId = datas[1];
		//找到全部关联的作者
		Conference checkEn = mongoTemplate.findById(conferenceId, Conference.class);
		if (checkEn == null) throw new EntityNotFoundException();

		//TODO: 当前返回的还是空, 需要进行持久化
		List<Author> authorList = mongoTemplate.find(new Query(new Criteria(conColName).is(checkEn.getConferenceName())),
				Author.class);
		List<Field> ans = new LinkedList<>();
		authorList.forEach((Author en) -> {
			String terms = en.getTerms();
			final String termSplitter = ",";
			Arrays.stream(terms.split(termSplitter)).forEach((String fieldName) -> {
				List<Field> fields = mongoTemplate.find(Query.query(new Criteria("fieldName").is(fieldName)),
						Field.class);
				ans.addAll(fields);
			});
		});
		return ans;
	}

	@Override
	public List<Field> fetchFieldList(int pageNum, int pageSize) {
		return mongoTemplate.find(new Query().with(PageRequest.of(pageNum, pageSize)), Field.class);
	}

}
