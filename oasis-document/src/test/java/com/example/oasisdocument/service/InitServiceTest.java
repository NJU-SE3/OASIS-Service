package com.example.oasisdocument.service;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InitServiceTest {
	private static final Logger logger = LoggerFactory.getLogger(InitServiceTest.class);
	@Autowired
	private AffiliationService affiliationService;

	@Test
	public void initCounterTest() {
		JSONObject obj = affiliationService.fetchAffiliationList(10, 10, "activeness");
		logger.info(obj.toJSONString());
	}

}
