package com.example.oasisdocument.service;

import com.example.oasisdocument.utils.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ReportServiceTest {
	@Autowired
	ReportService reportService;

	@Test
	public void getPaperTrendTest1() {
		final String id = "37061187000";
		//计数
		List<Pair<Integer, Double>> ans = reportService.getPaperTrend("count", id);
		assertThat(ans).isNotNull();
	}
}
