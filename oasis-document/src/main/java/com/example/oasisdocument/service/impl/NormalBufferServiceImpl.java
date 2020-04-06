package com.example.oasisdocument.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.analysis.NormalBuffer;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.service.NormalBufferSerivce;
import com.example.oasisdocument.utils.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NormalBufferServiceImpl implements NormalBufferSerivce {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private PageHelper pageHelper;

	@Override
	@Async
	public void storeAuthorFieldSummary() {
		final String type = "author_field_summary";
		final int pageSize = 10;
		JSONArray ans = new JSONArray();

		Map<String, Long> hash = new HashMap<>();
		for (Author author : authorRepository.findAll()) {
			for (String fieldName : Author.getAllFields(author)) {
				hash.put(fieldName, hash.getOrDefault(fieldName, 0L) + 1);
			}
		}

		for (String key : hash.keySet()) {
			JSONObject obj = new JSONObject();
			obj.put("fieldName", key);
			obj.put("count", hash.get(key));
			ans.add(obj);
		}
		ans.sort((o1, o2) -> {
			JSONObject ob1 = JSONObject.parseObject(JSON.toJSONString(o1)),
					ob2 = JSONObject.parseObject(JSON.toJSONString(o2));
			return (int) (ob2.getDouble("count") - ob1.getDouble("count"));
		});
		List<Object> li = pageHelper.of(ans, pageSize, 0);
		// store
		NormalBuffer buffer =
				mongoTemplate.findOne(Query.query(new Criteria("type").is(type)), NormalBuffer.class);
		if (buffer == null) buffer = new NormalBuffer();
		buffer.setContent(JSON.toJSONString(li));
		buffer.setType(type);
		mongoTemplate.save(buffer);

	}

	@Override
	@Cacheable(cacheNames = "loadAuthorFieldSummary", unless = "#result==null")
	public JSONArray loadAuthorFieldSummary() {
		final String type = "author_field_summary";
		NormalBuffer buffer =
				mongoTemplate.findOne(Query.query(new Criteria("type").is(type)), NormalBuffer.class);
		if (null == buffer) throw new EntityNotFoundException();
		return JSON.parseArray(buffer.getContent());
	}
}
