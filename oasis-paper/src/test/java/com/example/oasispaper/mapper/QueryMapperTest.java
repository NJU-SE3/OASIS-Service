package com.example.oasispaper.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("prod")
public class QueryMapperTest {
    Logger logger = LoggerFactory.getLogger(QueryMapperTest.class);

    @Autowired
    private QueryMapper queryMapper;

    //基本关键词搜索测试
    @Test
    public void mapQueryTest() {
        final String key = "java";
        int cur = queryMapper.queryAll(key).size();
        assertNotEquals(0, cur);
        assertEquals(queryMapper.queryAll(key).size(), cur);
    }

    @Test
    public void nameQueryTest() {
        final String key = "John";
        int cur = queryMapper.queryAll(key).size();
        assertEquals(queryMapper.queryAll(key).size(), cur);
    }

    //列表性测试
    @Test
    public void loopTest() {
        final String[] keys = {
                "jva", "beijing", "nju",
                "kvs", "morning", "size"
        };
        for (String key : keys) {
            int cur = queryMapper.queryAll(key).size();
            assertEquals(queryMapper.queryAll(key).size(), cur);
        }
    }

}
