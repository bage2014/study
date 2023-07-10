package com.bage.study.best.practice.cache;

public interface CacheService {
    void cache(String key, Object data);

    void remove(String key);

}
