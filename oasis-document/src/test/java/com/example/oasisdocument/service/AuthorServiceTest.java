package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class AuthorServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(AuthorServiceTest.class);

	@Autowired
	private AuthorService authorService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void fetchAuthorListTest1() {
		JSONArray authorList = authorService.fetchAuthorList(0,10);
		assertThat(authorList.size()).isGreaterThanOrEqualTo(0);
	}

	@Test
	public void fetchAuthorListTest2() {
		Field field = mongoTemplate.findOne(new Query(), Field.class);
		assertThat(field).isNotNull();
	}

	@Test
	public  void getSummaryTest1(){
		authorService.fetchAuthorSummaryUponField();
	}
}
