package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.service.ConferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class ConferenceServiceImpl implements ConferenceService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Conference fetchEnById(String id) {
		Conference en = mongoTemplate.findById(id, Conference.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}
}
