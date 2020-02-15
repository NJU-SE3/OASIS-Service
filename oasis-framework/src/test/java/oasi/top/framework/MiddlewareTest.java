package oasi.top.framework;

import oasi.top.framework.service.MsgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertThat;


/**
 * Test fot performance of Redis , rabbit
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MiddlewareTest {
    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    MsgService msgService;

    @Test
    public void redisTemplateTest() {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("k1", "v1");
        String k1 = ops.get("k1");
        assert k1 != null;
        assert k1.equals("v1");
    }

    @Test
    public void rabbitTest(){
        msgService.send("Hello world!");
    }
}
