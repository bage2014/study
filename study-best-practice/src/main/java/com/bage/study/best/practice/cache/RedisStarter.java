package com.bage.study.best.practice.cache;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.ValueOperations;

import javax.annotation.Resource;

public class RedisStarter implements CommandLineRunner {

    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Override
    public void run(String... strings) throws Exception {

        valueOperations.set("foo","bar bar");
        System.out.println(valueOperations.get("foo"));
    }

}