package com.example.oasispaper;

import oasi.top.framework.service.MsgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OasisPaperApplicationTests {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    public void redisTemplateTest() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("k1", "v1");
        String k1 = ops.get("k1");
        assert k1 != null;
        assert k1.equals("v1");
    }

}
