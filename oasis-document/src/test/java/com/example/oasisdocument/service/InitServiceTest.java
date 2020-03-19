package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class InitServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(InitServiceTest.class);
	@Autowired
	private InitializationService initializationService;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void init() {
		mongoTemplate.dropCollection(CounterBaseEntity.class);
	}
	@Test
	public void initCounterTest() {
		initializationService.initCounterPOJO();
		List<CounterBaseEntity> list = mongoTemplate.findAll(CounterBaseEntity.class);
		int size = list.size();
		initializationService.initCounterPOJO();
		assertEquals(size, mongoTemplate.findAll(CounterBaseEntity.class).size());
	}


	@After
	public void clean() {
	}
}
