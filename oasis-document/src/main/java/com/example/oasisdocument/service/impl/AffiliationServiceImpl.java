package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.service.AffiliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AffiliationServiceImpl implements AffiliationService {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	@Cacheable(cacheNames = "fetchEnById", unless = "#result==null")
	public Affiliation fetchEnById(String id) {
		Affiliation en = mongoTemplate.findById(id, Affiliation.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}

	@Override
	@Cacheable(cacheNames = "fetchAuthorsByAffiliationName", unless = "#result==null")
	public List<Author> fetchAuthorsByAffiliationName(String affName) {
		return mongoTemplate.find(new Query(new Criteria("affiliationName").is(affName)),
				Author.class);
	}

	@Override
	@Cacheable(cacheNames = "fetchAffiliationList", unless = "#result==null")
	public List<Affiliation> fetchAffiliationList(int pageNum, int pageSize) {
		return mongoTemplate.find(new Query().with(PageRequest.of(pageNum, pageSize)),
				Affiliation.class);
	}
}
