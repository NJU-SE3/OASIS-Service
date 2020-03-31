package com.example.oasisdocument.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class GraphControllerTest {
	private final static Logger logger = LoggerFactory.getLogger(GraphControllerTest.class);
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;


	private Cookie cookies[];

	@Before
	public void prepare() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void authorGraphTest1() throws Exception {
		String uri = "/graph/author";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put("id", Collections.singletonList("37271338700"));
		MvcResult result = mockMvc.perform(get(uri)
				.params(params))
				.andExpect(status().isOk())
				.andReturn();
		String body = result.getResponse().getContentAsString();
		logger.info(body);
	}
}
