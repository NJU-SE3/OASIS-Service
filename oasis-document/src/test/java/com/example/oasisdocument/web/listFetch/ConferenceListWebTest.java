package com.example.oasisdocument.web.listFetch;

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
public class ConferenceListWebTest {
	private static final Logger logger = LoggerFactory.getLogger(ConferenceListWebTest.class);
	private static final String uri = "/conference/list";
	//默认以json传输
	private static final String defaultContentType = "application/json";
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
	public void fetchTest1() throws Exception {
		final Field en = mongoTemplate.findOne(new Query(), Field.class);
		assertThat(en).isNotNull();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put("refinement", Collections.singletonList("field:" + en.getId()));
		MvcResult result = mockMvc.perform(get(uri)
				.params(params)
				.contentType(defaultContentType))
				.andExpect(status().isOk())
				.andReturn();
		assertThat(result.getResponse()).isNotNull();
		logger.info(result.getResponse().getContentAsString());
	}

	@Test
	public void fetchTest2() throws Exception {
		final Field en = mongoTemplate.findOne(new Query(), Field.class);
		assertThat(en).isNotNull();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.put("refinement", Collections.singletonList("field:233"));

		MvcResult result = mockMvc.perform(get(uri)
				.params(params)
				.contentType(defaultContentType))
				.andExpect(status().isNotFound())
				.andReturn();
		assertThat(result.getResponse()).isNotNull();
		logger.info(result.getResponse().getContentAsString());
	}

}
