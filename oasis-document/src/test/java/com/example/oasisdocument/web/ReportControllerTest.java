package com.example.oasisdocument.web;

import com.example.oasisdocument.model.docs.Author;
import com.example.oasisdocument.model.docs.extendDoc.Field;
import org.junit.Before;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportControllerTest {
	private static final Logger logger = LoggerFactory.getLogger(ReportControllerTest.class);
	private MockMvc mockMvc;
	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private MongoTemplate mongoTemplate;

	private Cookie cookies[];

	@Before
	public void prepare() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void getTrendWebTest1() throws Exception {
		final String uri = "/report/paper/trend/year";
		//空参数测试
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		MvcResult result = mockMvc.perform(get(uri)
				.params(params)
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		assertThat(result.getResponse()).isNotNull();
	}

	/**
	 * count 趋势查询
	 */
	@Test
	public void getTrendWebTest2() throws Exception {
		final String uri = "/report/paper/trend/year";
		final Field en = mongoTemplate.findOne(new Query(), Field.class);
		assertThat(en).isNotNull();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put("baseline", Collections.singletonList("count"));
		params.put("refinement", Collections.singletonList(en.getId()));
		MvcResult result = mockMvc.perform(get(uri)
				.params(params)
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		assertThat(result.getResponse()).isNotNull();
	}

	/**
	 * heat 趋势查询
	 */
	@Test
	public void getTrendWebTest3() throws Exception {
		final String uri = "/report/paper/trend/year";
		final Author en = mongoTemplate.findOne(new Query(), Author.class);
		assertThat(en).isNotNull();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put("baseline", Collections.singletonList("heat"));
		params.put("refinement", Collections.singletonList(en.getId()));
		MvcResult result = mockMvc.perform(get(uri)
				.params(params)
				.contentType("application/json"))
				.andExpect(status().isOk())
				.andReturn();
		assertThat(result.getResponse()).isNotNull();
	}
}
