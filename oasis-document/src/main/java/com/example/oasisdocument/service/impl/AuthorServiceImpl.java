package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.service.AuthorService;
import com.example.oasisdocument.service.InitializationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private InitializationService initializationService;
	private static final String refineSplitter = ":";

	//author 导入
	//涉及到 affiliation 和 field 的导入
	@Override
	@Async
	public void insert(Author entity) {
		if (null == mongoTemplate.findById(entity.getId(), Author.class)) {
			String affName = entity.getAffiliationName();
			String[] filedNameList = entity.getField().split(",");
			//导入机构
			Affiliation affEn = mongoTemplate.findOne(Query.query(new Criteria("affiliationName").is(affName)),
					Affiliation.class);
			if (null == affEn) {
				if (!affName.isEmpty()) {
					affEn = new Affiliation();
					affEn.setAffiliationName(affName);
					affEn = mongoTemplate.save(affEn);
				}
			}
			//导入field
			List<Field> fields = new LinkedList<>();
			for (String fieldName : filedNameList) {
				Field fieldEn = mongoTemplate.findOne(Query.query(new Criteria("fieldName").is(fieldName)),
						Field.class);
				if (null == fieldEn) {
					if (affName.isEmpty()) continue;
					fieldEn = new Field();
					fieldEn.setFieldName(fieldName);
					fieldEn = mongoTemplate.save(fieldEn);
				}
				fields.add(fieldEn);
			}
			entity.setAffiliationEntity(affEn);
			entity.setFieldList(fields);
			authorRepository.save(entity);
		}
	}

	@Override
	public Author fetchEnById(String id) {
		List<Author> list = authorRepository.findAllById(id);
		if (list.isEmpty()) throw new EntityNotFoundException();
		return list.get(0);
	}

	@Override
	public List<Author> fetchAuthorList(int pageNum, int pageSize) {
		return mongoTemplate.find(new Query().with(PageRequest.of(pageNum, pageSize)),
				Author.class);
	}

	//查找某一个限定条件下的作者列表
	@Override
	public List<Author> fetchAuthorList(String refinement) {
		final String affiCol = "affiliationName", fieldCol = "field";
		String[] strings = refinement.split(refineSplitter);
		if (strings.length != 2) throw new BadReqException();
		String id = strings[1];
		if (strings[0].equals("affiliation")) {
			Affiliation aff = mongoTemplate.findOne(Query.query(new Criteria("id").is(id)), Affiliation.class);
			if (null == aff) throw new EntityNotFoundException();
			return mongoTemplate.find(Query.query(new Criteria(affiCol).regex(aff.getAffiliationName())), Author.class);
		} else if (strings[0].equals("field")) {
			Field field = mongoTemplate.findOne(Query.query(new Criteria("id").is(id)), Field.class);
			if (null == field) throw new EntityNotFoundException();
			return mongoTemplate.find(Query.query(new Criteria(fieldCol).regex(field.getFieldName())), Author.class);
		} else throw new BadReqException();
	}

}
