package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import com.example.oasisdocument.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FieldServiceImpl implements FieldService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Field fetchEnById(String id) {
		Field en = mongoTemplate.findById(id, Field.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}
}
