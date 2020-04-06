package com.example.oasisdocument.dao;

import com.example.oasisdocument.model.graph.nodes.AuthorNeo;
import com.example.oasisdocument.model.graph.nodes.PaperNeo;
import com.example.oasisdocument.repository.graph.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphNeoDaoTest {
	private Logger logger = LoggerFactory.getLogger(GraphNeoDaoTest.class);
	@Autowired
	private PaperNeoRepo paperNeoRepo;
	@Autowired
	private AuthorNeoRepo authorNeoRepo;
	@Autowired
	private AffiliationNeoRepo affiliationNeoRepo;
	@Autowired
	private ConferenceNeoRepo conferenceNeoRepo;
	@Autowired
	private FieldNeoRepo fieldNeoRepo;

	@Test
	public void authorNeoTest1() {
		final String xid = authorNeoRepo.findAll().iterator().next().getXid();
		AuthorNeo neo = authorNeoRepo.findByXid(xid);
		assertThat(neo).isNotNull();
	}

	@Test
	public void authorNeoTest2() {
		AuthorNeo neo = authorNeoRepo.findByXid("");
		assertThat(neo).isNull();
	}

	@Test
	public void paperNeoTest1() {
		final String xid = paperNeoRepo.findAll().iterator().next().getXid();
		PaperNeo neo = paperNeoRepo.findByXid(xid);
		assertThat(neo).isNotNull();
		assertThat(neo.getXid()).isEqualTo(xid);
	}

	@Test
	public void affNeoTest1() {
		final String xid = affiliationNeoRepo.findAll().iterator().next().getXid();
		assertThat(affiliationNeoRepo.findByXid(xid)).isNotNull();
	}

	@Test
	public void conferenceNeoTest1() {
		final String xid = conferenceNeoRepo.findAll().iterator().next().getXid();
		assertThat(conferenceNeoRepo.findByXid(xid)).isNotNull();
	}

}
