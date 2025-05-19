package com.bage.study.best.practice.trial.redis;

import com.bage.study.best.practice.cache.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 主要包括：
 * <p>
 * 1、value值很长很大
 * 2、集合存储过多的元素
 * 3、key上亿
 *
 * https://juejin.cn/post/7298989375370166298
 *
 * https://juejin.cn/post/7303719808880590886
 *
 *
 */
@Slf4j
@Component
public class BasicRedisService {
    private String prefix = "redis_cache_basic_prefix_";
    private int maxCount = 100000;
    private CacheService cacheService;

    public BasicRedisService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    public int random() {
        String data = UUID.randomUUID().toString();
        String key = "random-key-" + UUID.randomUUID().toString() + "-" + 1;
        log.info("randomKey key = {}", key);
        getService().cache(data, "-random-key-" + data + "-" + 1);
        return 1;
    }

    private CacheService getService() {
        return cacheService;
    }

    public int init(Integer count) {
        if(count == null){
            count = 100000;
        }
        maxCount = count;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            getService().cache(prefix + i, "-init-" + UUID.randomUUID().toString() + "-");
        }
        log.info("time cost：" + (System.currentTimeMillis() - startTime));
        return 1;
    }

    public String get() {
        return get(new Random().nextInt(maxCount));
    }

    public String get(Integer index) {
        Object value = getService().get(prefix + index);
        return value == null ? null : value.toString();
    }

    public String get(String key) {
        Object value = getService().get(key);
        return value == null ? null : value.toString();
    }

}
