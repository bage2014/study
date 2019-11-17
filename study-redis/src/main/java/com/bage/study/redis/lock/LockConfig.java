package com.bage.study.redis.lock;

public class LockConfig {
    /**
     * 锁的key
     */
    private String key;
    private long sleepMillis = 500;
    private long expiredSecond = 5;

    private String ip = "127.0.0.1"; // 机器ip
    private long timestamp = System.currentTimeMillis(); // 时间戳
    private String threadId = String.valueOf(Thread.currentThread().getId()); // 线程ID

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public long getSleepMillis() {
        return sleepMillis;
    }

    public void setSleepMillis(long sleepMillis) {
        this.sleepMillis = sleepMillis;
    }

    public long getExpiredSecond() {
        return expiredSecond;
    }

    public void setExpiredSecond(long expiredSecond) {
        this.expiredSecond = expiredSecond;
    }
}
