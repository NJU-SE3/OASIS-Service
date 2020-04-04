package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONArray;
import com.example.oasisdocument.exceptions.BadReqException;
import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.extendDoc.Conference;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FieldServiceTest {
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private FieldService fieldService;

	@Test
	public void fetchListTest1() {
		JSONArray fieldList = fieldService.fetchFieldList(0, 10);
		assertThat(fieldList).isNotNull();
	}

	@Test
	public void fetchListTest2() {
		final String id = mongoTemplate.findOne(Query.query(new Criteria()), Conference.class).getId();
		final String refine = "conference:" + id;
		//2010 Fourth International Conference on Research Challenges in Information Science (RCIS)
		JSONArray fieldList = fieldService.fetchFieldList(refine, 0, 10);
		assertThat(fieldList).isNotNull();
		assertThat(fieldList.size()).isGreaterThanOrEqualTo(1);
	}

	@Test(expected = BadReqException.class)
	public void fetchListTest3() {
		final String refine = "conference";
		JSONArray fieldList = fieldService.fetchFieldList(refine, 0, 10);
	}

	@Test(expected = EntityNotFoundException.class)
	public void fetchListTest4() {
		JSONArray fieldList = fieldService.fetchFieldList("confernece:233", 0, 10);
	}
}
