package com.bage.study.rca.demo.supplier.fault;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 故障注入器：仅用于 RCA demo，生产代码严禁引入。
 * 支持：堆内存一次性泄漏 / 定时持续泄漏、CPU 忙等、线程泄漏。
 */
@Component
public class FaultInjector {

    /** 静态强引用集合：GC 无法回收，模拟典型内存泄漏（缓存只加不清）。 */
    private static final List<byte[]> LEAKED_MEMORY = Collections.synchronizedList(new ArrayList<>());

    private final List<Thread> leakedThreads = Collections.synchronizedList(new ArrayList<>());

    private final ScheduledExecutorService leakScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "fault-memory-leaker");
        thread.setDaemon(true);
        return thread;
    });

    private ScheduledFuture<?> leakTask;

    /** 一次性泄漏指定 MB 堆内存。 */
    public synchronized int leakOnce(int mb) {
        LEAKED_MEMORY.add(new byte[mb * 1024 * 1024]);
        return LEAKED_MEMORY.size();
    }

    /** 每秒持续泄漏 mbPerSecond MB，直到调用 stopLeak / reset。 */
    public synchronized boolean startLeak(int mbPerSecond) {
        if (leakTask != null) {
            return false;
        }
        leakTask = leakScheduler.scheduleAtFixedRate(
                () -> LEAKED_MEMORY.add(new byte[mbPerSecond * 1024 * 1024]),
                0, 1, TimeUnit.SECONDS);
        return true;
    }

    public synchronized boolean stopLeak() {
        if (leakTask == null) {
            return false;
        }
        leakTask.cancel(false);
        leakTask = null;
        return true;
    }

    public int clearMemory() {
        synchronized (LEAKED_MEMORY) {
            int blocks = LEAKED_MEMORY.size();
            LEAKED_MEMORY.clear();
            return blocks;
        }
    }

    /**
     * CPU 忙等：持续占用一个核，配合并发调用可打满多核。
     * 返回黑洞值防止 JIT 把循环优化掉。
     */
    public long burnCpu(int seconds) {
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(seconds);
        long blackhole = System.nanoTime();
        while (System.nanoTime() < deadline) {
            blackhole += Long.rotateLeft(blackhole ^ System.nanoTime(), 7);
        }
        return blackhole;
    }

    /** 创建 count 个永久 WAITING/TIMED_WAITING 的线程，模拟线程泄漏（单次封顶 2000）。 */
    public int spawnThreads(int count) {
        int target = Math.min(Math.max(count, 1), 2000);
        synchronized (leakedThreads) {
            for (int i = 0; i < target; i++) {
                Thread thread = new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            Thread.sleep(Long.MAX_VALUE);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }, "fault-leaked-thread-" + leakedThreads.size());
                thread.setDaemon(true);
                thread.start();
                leakedThreads.add(thread);
            }
            return leakedThreads.size();
        }
    }

    public int interruptThreads() {
        synchronized (leakedThreads) {
            int count = leakedThreads.size();
            leakedThreads.forEach(Thread::interrupt);
            leakedThreads.clear();
            return count;
        }
    }

    public synchronized void reset() {
        stopLeak();
        clearMemory();
        interruptThreads();
    }
}
