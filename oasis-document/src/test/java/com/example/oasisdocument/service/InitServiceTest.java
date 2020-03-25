package com.example.oasisdocument.service;

import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.repository.docs.PaperRepository;
import com.example.oasisdocument.utils.ComputeUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class InitServiceTest {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ReportService reportService;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private PaperRepository paperRepository;
	@Autowired
	private BaseService baseService;
	@Autowired
	private ComputeUtil computeUtil;

	@Before
	public void init() {
	}

	@Test
	public void initCounterTest() {
	}


	@After
	public void clean() {
	}
}
