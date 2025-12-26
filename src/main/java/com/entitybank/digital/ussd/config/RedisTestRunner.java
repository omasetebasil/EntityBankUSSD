package com.entitybank.digital.ussd.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTestRunner implements CommandLineRunner {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) {
        redisTemplate.opsForValue().set("test", "Hello Redis");
        System.out.println("Redis test value: " + redisTemplate.opsForValue().get("test"));
    }
}

