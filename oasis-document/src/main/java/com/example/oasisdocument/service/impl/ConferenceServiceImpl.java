package com.example.oasisdocument.service.impl;

import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.service.ConferenceService;
import com.example.oasisdocument.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConferenceServiceImpl implements ConferenceService {
	private static final String refineSplitter = ":";

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private CounterService counterService;

	@Override
	public Conference fetchEnById(String id) {
		Conference en = mongoTemplate.findById(id, Conference.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}

	@Override
	@Cacheable(cacheNames = "fetchConferenceList", unless = "#result==null")
	public List<Conference> fetchConferenceList(int pageNum, int pageSize) {
		return mongoTemplate.find(new Query().with(PageRequest.of(pageNum, pageSize)), Conference.class);
	}

	@Override
	@Cacheable(cacheNames = "fetchConferenceList", unless = "#result==null")
	public List<Conference> fetchConferenceList(String refinement) {
		final String fieldKey = "field", conNameCol = "conferenceName";
		String[] datas = refinement.split(refineSplitter);
		if (datas.length != 2 || !datas[0].equals(fieldKey)) throw new BadReqException();
		String fieldId = datas[1];
		//找到领域
		CounterBaseEntity en = counterService.getSummaryInfo(fieldId);
		if (null == en) throw new EntityNotFoundException();
		List<String> confNames = en.getPaperList().stream()
				.map((String pid) -> {
					Paper paper = mongoTemplate.findById(pid, Paper.class);
					return paper.getConference();
				}).collect(Collectors.toList());
		List<Conference> ans = confNames.stream()
				.map((String name) -> {
					List<Conference> conferences = mongoTemplate
							.find(Query.query(new Criteria(conNameCol).is(name)), Conference.class);
					return conferences.get(0);
				}).collect(Collectors.toList());
		return ans;
	}
}
