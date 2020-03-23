package com.example.oasisdocument.service;

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
public class ConferenceServiceTest {
	@Autowired
	private ConferenceService conferenceService;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	public void fetchConferenceListTest1() {
		List<Conference> conferenceList = conferenceService.fetchConferenceList(0, 10);
		assertThat(conferenceList).isNotNull();
		assertThat(conferenceList.size()).isGreaterThanOrEqualTo(1);
	}

	@Test
	public void fetchConferenceListTest2() {
		String id = mongoTemplate.findOne(Query.query(new Criteria()), Field.class).getId();
		final String refinement = "field:" + id;
		List<Conference> conferenceList = conferenceService.fetchConferenceList(refinement);
		assertThat(conferenceList).isNotNull();
	}

	@Test(expected = BadReqException.class)
	public void fetchConferenceListTest3() {
		final String refinement = "field";
		List<Conference> conferenceList = conferenceService.fetchConferenceList(refinement);
	}

	@Test(expected = EntityNotFoundException.class)
	public void fetchConferenceListTest4() {
		final String refinement = "field:233";
		List<Conference> conferenceList = conferenceService.fetchConferenceList(refinement);

	}

}
