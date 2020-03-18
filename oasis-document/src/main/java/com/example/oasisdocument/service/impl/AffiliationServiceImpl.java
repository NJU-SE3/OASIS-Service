package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.service.AffiliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class AffiliationServiceImpl implements AffiliationService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Affiliation fetchEnById(String id) {
		Affiliation en = mongoTemplate.findById(id, Affiliation.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}
}
