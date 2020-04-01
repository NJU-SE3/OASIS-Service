package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Affiliation;
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

import static org.assertj.core.api.Assertions.assertThat;


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
		final String xid = mongoTemplate.findOne(new Query(), Field.class).getId();
		JSONObject o = graphService.fieldMapViaId("5e7a232db04a431baf379654");
		assertThat(o).isNotNull();
		logger.info(o.toJSONString());
	}

	@Test
	public void authorMapTest1() {
		final String xid = mongoTemplate.findOne(new Query(), Author.class).getId();
		JSONObject o = graphService.centeralAuthor(xid);
		assertThat(o).isNotNull();
	}

	@Test
	public void affMapTest1() {
		final String xid = mongoTemplate.findOne(new Query(), Affiliation.class).getId();
		JSONObject o = graphService.affMapViaId(xid);
		assertThat(o).isNotNull();
		logger.info(o.toJSONString());
	}

}
