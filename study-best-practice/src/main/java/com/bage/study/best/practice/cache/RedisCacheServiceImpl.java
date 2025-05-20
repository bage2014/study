package com.bage.study.best.practice.cache;

import com.bage.study.best.practice.utils.JsonUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.concurrent.TimeUnit;

public class RedisCacheServiceImpl implements CacheService {


    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;
    @Resource(name = "redisTemplate")
    private ZSetOperations<String, String> zSetOperations;

    @Override
    public void cache(String key, Object data) {
        valueOperations.set(key, JsonUtils.toJson(data), 2, TimeUnit.HOURS);
    }

    @Override
    public Object get(String key) {
        return valueOperations.get(key);
    }

    @Override
    public void remove(String key) {
        valueOperations.getAndDelete(key);
    }

    public Object cacheSet(String key, String value) {
        return zSetOperations.add(key, value, 1.0);
    }


    public Object randomGet(String key) {
        return zSetOperations.randomMember(key);
    }

    public Long size(String key) {
        return zSetOperations.size(key);
    }


}
