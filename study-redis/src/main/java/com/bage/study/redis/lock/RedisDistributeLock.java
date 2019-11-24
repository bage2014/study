package com.bage.study.redis.lock;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class RedisDistributeLock implements DistributeLock {

    RedisTemplete redisTemplete = new RedisTemplete();
    LockConfig lockConfig = new LockConfig();

    public RedisDistributeLock(String key) {
        lockConfig.setKey(key);
    }

    /**
     * 暂未实现
     */
    public void lock() {
        throw new RuntimeException("not support now");
    }

    /**
     * 暂未实现
      * @throws InterruptedException
     */
    public void lockInterruptibly() throws InterruptedException {
        throw new RuntimeException("not support now");
    }

    /**
     * 利用原子锁进行上锁
     * @return
     */
    public boolean tryLock() {

        String key = buildLockKey();
        String value = buildLockValue();

        return redisTemplete.setIfNotExist(key, value, lockConfig.getExpiredSecond());
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long timeUnit = unit.toMillis(time);
        long now = System.currentTimeMillis();

        while (System.currentTimeMillis() > now + timeUnit) {
            if (tryLock()) {
                return true;
            }
            sleepSomeTime();
        }
        return false;

    }

    private void sleepSomeTime() {
        try {
            Thread.sleep(lockConfig.getSleepMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void unlock() {
        String key = buildLockKey();
        String value = buildLockValue();

        List<String> keys = Arrays.asList(key);
        List<String> args = Arrays.asList(value);

        redisTemplete.exvel(LuaScript.UNLOCK_SCRIPT, keys, args);
    }

    public Condition newCondition() {
        return null;
    }


    private String buildLockValue() {
        StringBuilder sb = new StringBuilder()
                .append(lockConfig.getIp())
                .append(".")
                .append(lockConfig.getThreadId())
                .append(".")
                .append(lockConfig.getKey());
        return sb.toString();
    }

    /**
     * key 组成： ip + 线程ID + key
     * @return
     */
    private String buildLockKey() {
        StringBuilder sb = new StringBuilder()
                .append(lockConfig.getIp())
                .append(".")
                .append(lockConfig.getThreadId())
                .append(".")
                .append(lockConfig.getKey());
        return sb.toString();
    }
}
