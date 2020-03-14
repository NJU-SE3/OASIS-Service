package com.example.oasisdocument.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ConcurrentHashMap;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class GraphServiceTest {
	@Autowired
	private GraphService graphService;

	@Test
	public void authorInitTest() {
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
		map.put("", "");
		map.get("");
		map.remove("");
	}
}
