package com.bage.study.best.practice.cache;

public interface CacheService {
    void cache(String key, Object data);
    Object get(String key);

    void remove(String key);

}
