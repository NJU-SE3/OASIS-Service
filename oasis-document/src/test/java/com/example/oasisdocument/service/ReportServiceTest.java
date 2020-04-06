package com.example.oasisdocument.service;

import com.example.oasisdocument.model.docs.counter.CounterBaseEntity;
import com.example.oasisdocument.utils.Pair;
import org.junit.Before;
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
public class ReportServiceTest {
	@Autowired
	ReportService reportService;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Before
	public void init() {
		//delete all cache
		Query query = new Query(Criteria.where("year").ne(-1));
		mongoTemplate.remove(query, CounterBaseEntity.class);
	}

	@Test
	public void getPaperTrendTest1() {
		final String id = "37063321100";
		//计数
		List<Pair<Integer, Double>> ans = reportService.getPaperTrend("heat", id);
		assertThat(ans).isNotNull();

	}


}
