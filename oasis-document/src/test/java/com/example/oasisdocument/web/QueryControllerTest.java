package com.example.oasisdocument.web;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.oasisdocument.model.docs.Paper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QueryControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;


    private Cookie cookies[];

    @Before
    public void prepare() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * URI : /api/query/paper/list
     * 初次数据查询
     */
    @Test
    public void queryCtlTest1() throws Exception {
        sampleQuery();
    }

    /**
     * URI : /api/query/paper/summary
     * 获取指定的查询id下的论文summary
     */
    @Test
    public void queryCtlTest2() throws Exception {
        summary();
    }


    private void sampleQuery() throws Exception {
        final String uri = "/query/paper/list";
        final int defaultPageSize = 10;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("query", Collections.singletonList("java"));
        params.put("returnFacets", Collections.singletonList("all"));
        MvcResult result = mockMvc.perform(get(uri)
                .params(params)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();
        assertThat(result.getResponse()).isNotNull();
        String body = result.getResponse().getContentAsString();
        JSONObject object = JSONObject.parseObject(body);
        //判定返回包合法结构
        assertThat(object.containsKey("papers")).isTrue();
        List<Paper> paperList = (List<Paper>) object.get("papers");
        //判定分页结果
        assertThat(paperList).isNotNull();
        assertThat(paperList.size()).isBetween(0, defaultPageSize);
        cookies = result.getResponse().getCookies();
    }


    private JSONObject summary() throws Exception {
        sampleQuery();
        String summaryUri = "/query/paper/summary";
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("refinements", Collections.singletonList(""));
        MvcResult result = mockMvc.perform(get(summaryUri)
                .cookie(cookies)
                .params(params))
                .andExpect(status().isOk())
                .andReturn();
        JSONObject response = JSON.parseObject(result.getResponse().getContentAsString());
        assertThat(response).isNotNull();
        return response;
    }
}
