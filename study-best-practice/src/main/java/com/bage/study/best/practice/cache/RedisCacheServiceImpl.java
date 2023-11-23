package com.bage.study.best.practice.cache;

import com.bage.study.best.practice.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

public class RedisCacheServiceImpl implements CacheService{


    @Resource(name="redisTemplate")
    private ValueOperations<String, String> valueOperations;

    @Override
    public void cache(String key, Object data) {
        valueOperations.set(key, JsonUtils.toJson(data),60 * 10, TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        valueOperations.getAndDelete(key);
    }

}
