package com.bage.study.best.practice.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JvmCacheServiceImpl implements CacheService {

    private Map<String, Object> map = new ConcurrentHashMap();

    @Override
    public void cache(String key, Object data) {
        map.put(key, data);
    }

    @Override
    public Object get(String key) {
        return map.get(key);
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }

}
