package com.example.oasisdocument.service;

import com.example.oasisdocument.exceptions.EntityNotFoundException;
import com.example.oasisdocument.model.docs.analysis.GraphEdge;
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


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GraphServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(GraphServiceTest.class);
	@Autowired
	private GraphService graphService;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Test(expected = EntityNotFoundException.class)
	public void fieldMapVIaIdTest1() {
		List<GraphEdge> edges = graphService.fieldMapViaId("23");
	}


}
