package io.summerx.framework.redis;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

public class SpringRedisTest {

    ApplicationContext applicationContext;
    RedisTemplate redisTemplate;

    void setup() {
        applicationContext = new ClassPathXmlApplicationContext("spring/spring-redis.xml");
        redisTemplate = applicationContext.getBean("redisTemplate", RedisTemplate.class);

    }

    void prepareRunTestCase1() {
        redisTemplate.opsForValue().set("x", "y");
    }

    @Test
    public void runTestCase1() {
        // 设定测试运行环境
        setup();
        // 准备数据
        prepareRunTestCase1();
        Assert.assertEquals("y", redisTemplate.opsForValue().get("x"));
    }
}
