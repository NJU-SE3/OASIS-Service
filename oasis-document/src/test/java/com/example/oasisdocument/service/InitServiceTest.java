package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.Paper;
import com.example.oasisdocument.repository.docs.AuthorRepository;
import com.example.oasisdocument.repository.docs.PaperRepository;
import com.example.oasisdocument.utils.ComputeUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class InitServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(InitServiceTest.class);
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private ReportService reportService;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private PaperRepository paperRepository;
	@Autowired
	private FieldService fieldService;
	@Autowired
	private CounterService counterService;
	@Autowired
	private BaseService baseService;
	@Autowired
	private ComputeUtil computeUtil;

	@Test
	public void initCounterTest() {

	}



	@After
	public void clean() {
	}
}
