package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.repository.docs.PaperRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class InitServiceTest {
	@Autowired
	private InitializationService initializationService;
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ReportService reportService;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private PaperRepository paperRepository;

	@Before
	public void init() {
	}

	@Test
	public void initCounterTest() {
		initializationService.initCounterPOJOSummary();
		List<CounterBaseEntity> enList = mongoTemplate.findAll(CounterBaseEntity.class);
		int size = enList.size();
		initializationService.initCounterPOJOSummary();
		enList = mongoTemplate.findAll(CounterBaseEntity.class);
		assertThat(enList.size()).isEqualTo(size);
	}

	@After
	public void clean() {
	}
}
