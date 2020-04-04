package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.service.AffiliationService;
import com.example.oasisdocument.service.CounterService;
import com.example.oasisdocument.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AffiliationServiceImpl implements AffiliationService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private GeneralJsonVO generalJsonVO;
	@Autowired
	private PageHelper pageHelper;
	@Autowired
	private CounterService counterService;

	@Override
	@Cacheable(cacheNames = "fetchEnById", unless = "#result==null")
	public Affiliation fetchEnById(String id) {
		Affiliation en = mongoTemplate.findById(id, Affiliation.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}


	@Override
	@Cacheable(cacheNames = "fetchAffiliationList", unless = "#result==null")
	public JSONArray fetchAffiliationList(int pageNum, int pageSize) {
		List<Affiliation> affiliationList = mongoTemplate.findAll(Affiliation.class);
		JSONArray array = new JSONArray();
		for (Affiliation affiliation : affiliationList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(affiliation.getId());
			array.add(generalJsonVO.affiliation2VO(affiliation, baseEntity));
		}
		return pageHelper.sortAndPage(array, pageSize, pageNum);
	}
}
