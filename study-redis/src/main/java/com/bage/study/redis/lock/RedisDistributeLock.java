package com.bage.study.redis.lock;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class RedisDistributeLock implements DistributeLock {

    RedisTemplete redisTemplete = new RedisTemplete();
    LockConfig lockConfig = new LockConfig();

    public RedisDistributeLock(String key) {
        lockConfig.setKey(key);
    }

    public void lock() {
        throw new RuntimeException("not support now");
    }

    public void lockInterruptibly() throws InterruptedException {
        throw new RuntimeException("not support now");
    }

    public boolean tryLock() {

        List<String> keys = Collections.emptyList();
        List<String> args = Collections.emptyList();

        return redisTemplete.exvel(LuaScript.LOCK_SCRIPT, keys, args);
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long timeUnit = unit.toMillis(time);
        long now = System.currentTimeMillis();

        while (System.currentTimeMillis() > now + timeUnit) {
            boolean isLocked = tryLock();
            if (isLocked) {
                return true;
            }
            sleepSomeTime();
        }
        return false;

    }

    private void sleepSomeTime() {
        try {
            Thread.sleep(lockConfig.getSleepTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unlock() {
        List<String> keys = Collections.emptyList();
        List<String> args = Collections.emptyList();
        redisTemplete.exvel(LuaScript.UNLOCK_SCRIPT, keys, args);
    }

    public Condition newCondition() {
        return null;
    }
}
