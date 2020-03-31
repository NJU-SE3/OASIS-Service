package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GraphServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(GraphServiceTest.class);
	@Autowired
	private GraphService graphService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void fieldMapVIaIdTest1() {
	}

	@Test
	public void authorMapTest1() {
		final String id = "37271338700";
		JSONObject o = graphService.centeralAuthor(id);
		System.out.println(o.toJSONString());
	}


}
