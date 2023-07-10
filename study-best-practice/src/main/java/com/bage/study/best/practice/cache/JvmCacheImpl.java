package com.bage.study.best.practice.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JvmCacheImpl implements CacheService {

    private Map<String, Object> map = new ConcurrentHashMap();

    @Override
    public void cache(String key, Object data) {
        map.put(key, data);
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }

    @Override
    public long size() {
        return map.size();
    }
}
