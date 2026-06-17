package com.study.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.atomic.AtomicLong;

public final class IdGenerator {

    private static final long EPOCH = 1704067200000L;
    private static final long WORKER_ID_BITS = 5L;
    private static final long DATA_CENTER_ID_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;

    private static final long MAX_WORKER_ID = (1L << WORKER_ID_BITS) - 1;
    private static final long MAX_DATA_CENTER_ID = (1L << DATA_CENTER_ID_BITS) - 1;
    private static final long SEQUENCE_MASK = (1L << SEQUENCE_BITS) - 1;

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    private static long workerId = 1L;
    private static long dataCenterId = 1L;
    private static final AtomicLong sequence = new AtomicLong(0L);
    private static long lastTimestamp = -1L;

    private IdGenerator() {
    }

    public static void init(long workerIdValue, long dataCenterIdValue) {
        if (workerIdValue < 0 || workerIdValue > MAX_WORKER_ID) {
            throw new IllegalArgumentException("Worker ID must be between 0 and " + MAX_WORKER_ID);
        }
        if (dataCenterIdValue < 0 || dataCenterIdValue > MAX_DATA_CENTER_ID) {
            throw new IllegalArgumentException("Data Center ID must be between 0 and " + MAX_DATA_CENTER_ID);
        }
        workerId = workerIdValue;
        dataCenterId = dataCenterIdValue;
    }

    public static long nextId() {
        long timestamp = getCurrentTimestamp();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + "ms");
        }

        if (timestamp == lastTimestamp) {
            sequence.set((sequence.get() + 1) & SEQUENCE_MASK);
            if (sequence.get() == 0) {
                timestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence.set(0L);
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (dataCenterId << DATA_CENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence.get();
    }

    public static String nextIdStr() {
        return String.valueOf(nextId());
    }

    public static String uuid() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    public static String uuidWithDash() {
        return java.util.UUID.randomUUID().toString();
    }

    public static String shortId() {
        return String.valueOf(System.currentTimeMillis()) + String.format("%04d", (int) (Math.random() * 10000));
    }

    private static long getCurrentTimestamp() {
        return LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private static long waitNextMillis(long lastTimestamp) {
        long timestamp = getCurrentTimestamp();
        while (timestamp <= lastTimestamp) {
            timestamp = getCurrentTimestamp();
        }
        return timestamp;
    }
}