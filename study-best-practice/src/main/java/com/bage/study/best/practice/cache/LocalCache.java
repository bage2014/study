package com.bage.study.best.practice.cache;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LocalCache {

    private Map<String, Object> map = new ConcurrentHashMap();

    public void cache(String key, Object data) {
        map.put(key, data);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public long size() {
        return map.size();
    }
}
