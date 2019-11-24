package com.bage.study.redis.lock;

public class DistributeLockBuilder {

    public DistributeLock getLock(String key) {
        return new RedisDistributeLock("com.bage.some.key");
    }

}
