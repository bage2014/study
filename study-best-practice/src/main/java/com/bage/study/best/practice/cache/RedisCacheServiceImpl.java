package com.bage.study.best.practice.cache;

import com.bage.study.best.practice.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

public class RedisCacheServiceImpl implements CacheService{


    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Override
    public void cache(String key, Object data) {
        valueOperations.set(key, JsonUtils.toJson(data));
    }

    @Override
    public void remove(String key) {
        valueOperations.set(key,null);
    }

}
