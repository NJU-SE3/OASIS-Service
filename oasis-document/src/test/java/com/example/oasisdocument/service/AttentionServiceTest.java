package com.example.oasisdocument.service;

import com.example.oasisdocument.options.AttentionDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AttentionServiceTest {
    private final static Logger logger = LoggerFactory.getLogger(AttentionServiceTest.class);
    private final static String testAuthorId = "37086943725";
    @Autowired
    private AttentionService attentionService;

    @Test
    public void attentionServiceTest1() {
        AttentionDTO result = attentionService.queryMaxAttention(testAuthorId, 2018);
        logger.info(String.valueOf(result));
        List<AttentionDTO> attentionDTOS = attentionService.batchQueryMaxAttention(testAuthorId, 2000, 2020);
        logger.info(String.valueOf(attentionDTOS.size()));
    }

    @Test
    public void attentionServiceTest2() {
        final String filedName = "power aware computing";
        List<AttentionDTO> list = attentionService.queryAttentionTrend(testAuthorId, filedName);
        logger.info(String.valueOf(list.size()));
    }
}
