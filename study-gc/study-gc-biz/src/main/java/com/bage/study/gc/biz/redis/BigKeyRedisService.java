package com.bage.study.gc.biz.redis;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 主要包括：
 * <p>
 * 1、value值很长很大
 * 2、集合存储过多的元素
 * 3、key上亿
 */
@Slf4j
public class BigKeyRedisService {

    private RedisService redisService;

    public BigKeyRedisService(RedisService redisService){
        this.redisService = redisService;
    }

    /**
     * redis 的 value值很大
     *
     * @param number
     * @return
     */
    public int bigValve(int number) {
        String data = "";
        for (int j = 0; j < number; j++) {
            data += UUID.randomUUID().toString();
        }
        String key = "big-key-" + UUID.randomUUID().toString() + "-" + number;
        log.info("key = {}", key);
        getService().set(key, "-big-value-" + data + "-" + number);
        return 1;
    }

    /**
     * 集合很大
     *
     * @param length
     * @return
     */
    public int bigCollection(int length) {
        List<Object> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(UUID.randomUUID().toString() + "-" + i);
        }
        String key = "big-collection-" + UUID.randomUUID().toString() + "-" + length;
        log.info("key = {}", key);
        getService().listSet(key, list);
        return length;
    }

    /**
     * 很多的普通的key + value
     *
     * @param number
     * @return
     */
    public int bigNumber(int number) {
        for (int i = 0; i < number; i++) {
            String data = UUID.randomUUID().toString();
            String key = "big-number-" + UUID.randomUUID().toString() + "-" + number;
            if (i == 0) {
                log.info("key = {}", key);
            }
            getService().set(data, "-big-number-" + data + "-" + i);
        }
        return number;
    }

    private RedisService getService() {
        return redisService;
    }

}
