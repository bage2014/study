package com.bage.study.redis.lock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class RedisDistributeLock implements DistributeLock {

    RedisTemplete redisTemplete = new RedisTemplete();

    public void lock() {
        throw new RuntimeException("not support now");
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new RuntimeException("not support now");
    }

    public boolean tryLock() {

        List<String> keys = Collections.emptyList();
        List<String> args = Collections.emptyList();

        return redisTemplete.exvel(LuaScript.LOCK_SCRIPT,keys,args);
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public void unlock() {
        List<String> keys = Collections.emptyList();
        List<String> args = Collections.emptyList();
        redisTemplete.exvel(LuaScript.UNLOCK_SCRIPT,keys,args);
    }

    public Condition newCondition() {
        return null;
    }
}
