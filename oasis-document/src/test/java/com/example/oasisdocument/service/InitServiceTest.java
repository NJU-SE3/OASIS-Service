package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
	@Autowired
	private InitializationService initializationService;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void init() {
		mongoTemplate.dropCollection(Affiliation.class);
		mongoTemplate.dropCollection(Conference.class);
		mongoTemplate.dropCollection(Field.class);
	}

	@Test
	public void affInitBasicTest1() {
		initializationService.initAffiliationBase();
		List<Affiliation> list = mongoTemplate.findAll(Affiliation.class);
		int size = list.size();
		initializationService.initAffiliationBase();
		assertEquals(size, mongoTemplate.findAll(Affiliation.class).size());
	}

	@Test
	public void initConferenceBasicTest1() {
		initializationService.initConferenceBasic();
		List<Conference> list = mongoTemplate.findAll(Conference.class);
		int size = list.size();
		initializationService.initConferenceBasic();
		assertEquals(size, mongoTemplate.findAll(Conference.class).size());
	}

	@Test
	public void initFieldBasicTest1() {
		initializationService.initFieldBasic();
		List<Field> list = mongoTemplate.findAll(Field.class);
		int size = list.size();
		initializationService.initFieldBasic();
		assertEquals(size, mongoTemplate.findAll(Field.class).size());
	}

	@Test
	public void initCounterTest() {
		initializationService.initFieldBasic();
		initializationService.initConferenceBasic();
		initializationService.initAffiliationBase();
		initializationService.initCounterPOJO();
		List<CounterBaseEntity> list = mongoTemplate.findAll(CounterBaseEntity.class);
		int size = list.size();
		initializationService.initCounterPOJO();
		assertEquals(size, mongoTemplate.findAll(CounterBaseEntity.class).size());
	}

	@After
	public void clean() {
		mongoTemplate.dropCollection(Affiliation.class);
		mongoTemplate.dropCollection(Conference.class);
		mongoTemplate.dropCollection(Field.class);
	}
}
