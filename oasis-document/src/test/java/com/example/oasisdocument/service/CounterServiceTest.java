package com.example.oasisdocument.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CounterServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(CounterServiceTest.class);

	@Autowired
	private AuthorService authorService;
	@Autowired
	private CounterService counterService;

	@Test
	public void counterServiceTest1() {
		counterService.getSummaryInfo("37087230617");
	}
}
