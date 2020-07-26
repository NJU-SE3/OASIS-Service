package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.VO.extendVO.GeneralJsonVO;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.service.ConferenceService;
import com.example.oasisdocument.service.CounterService;
import com.example.oasisdocument.service.IntermeService;
import com.example.oasisdocument.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.oasisdocument.service.IntermeService.affCounterCollection;
import static com.example.oasisdocument.service.IntermeService.conferenceCounterCollection;

@Service
public class ConferenceServiceImpl implements ConferenceService {
	private static final String refineSplitter = ":";

	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private CounterService counterService;
	@Autowired
	private IntermeService intermeService;
	@Autowired
	private GeneralJsonVO generalJsonVO;
	@Autowired
	private PageHelper pageHelper;

	@Override
	@Cacheable(cacheNames = "fetchEnById", unless = "#result==null")
	public Conference fetchEnById(String id) {
		Conference en = mongoTemplate.findById(id, Conference.class);
		if (null == en) throw new EntityNotFoundException();
		return en;
	}

	@Override
	public JSONObject fetchConferenceList(int pageNum, int pageSize, String rankKey) {
		//一级缓存命中
		Pageable pageable = PageRequest.of(pageNum, pageSize);
		JSONArray data = new JSONArray();
		long itemCnt = mongoTemplate.count(new Query(), Conference.class);
		Sort sort = Sort.by(Sort.Direction.DESC, rankKey);
		if (intermeService.intermeExist(conferenceCounterCollection)) {
			List<CounterBaseEntity> bufferList = mongoTemplate.find(new Query()
							.with(sort)
							.with(pageable),
					CounterBaseEntity.class, conferenceCounterCollection);
			for (CounterBaseEntity entity : bufferList) {
				data.add(conferenceBuffer2VO(entity));
			}
		} else {
			//缓存未命中 , 随机返回
			//异步缓存
			intermeService.genConferenceCounter();
			//随机返回pageable条目
			List<Conference> entities = mongoTemplate.find(new Query()
					.with(pageable).with(sort), Conference.class);
			for (Conference en : entities) {
				CounterBaseEntity baseEntity = counterService.getSummaryInfo(en.getId());
				data.add(generalJsonVO.conference2VO(en, baseEntity));
			}
		}
		JSONObject ans = new JSONObject();
		ans.put("data", data);
		ans.put("itemCnt", itemCnt);
		return ans;
	}

	private JSONObject conferenceBuffer2VO(CounterBaseEntity entity) {
		String uid = entity.getCheckId();
		Conference conference = mongoTemplate.findById(uid, Conference.class);
		if (null == conference) return new JSONObject();

		return generalJsonVO.conference2VO(conference, entity);
	}

	@Override
	@Cacheable(cacheNames = "fetchConferenceList", unless = "#result==null")
	public JSONArray fetchConferenceList(String refinement, int pageNum, int pageSize) {
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
		Set<Conference> conferenceList = confNames.stream()
				.map((String name) -> {
					List<Conference> conferences = mongoTemplate
							.find(Query.query(new Criteria(conNameCol).is(name)), Conference.class);
					return conferences.get(0);
				}).collect(Collectors.toSet());
		JSONArray array = new JSONArray();
		for (Conference conference : conferenceList) {
			CounterBaseEntity baseEntity = counterService.getSummaryInfo(conference.getId());
			array.add(generalJsonVO.conference2VO(conference, baseEntity));
		}
		return pageHelper.sortAndPage(array, pageSize, pageNum);
	}
}
