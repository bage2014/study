package com.bage.study.gc.jdk17;

import com.bage.study.gc.biz.redis.RedisService;
import com.bage.study.gc.biz.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisServiceImpl implements RedisService {

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Override
    public boolean set(String key, Object value) {
        valueOperations.set(key, JsonUtils.toJson(value));
        return true;
    }

    @Override
    public boolean delete(String key, Object value) {
        valueOperations.getAndDelete(key);
        return true;
    }

    @Override
    public boolean listSet(String key, List<Object> value) {
        return false;
    }

    @Override
    public boolean listAdd(String key, Object value) {
        return false;
    }

    @Override
    public boolean listRemove(String key, int inded) {
        return false;
    }
}
