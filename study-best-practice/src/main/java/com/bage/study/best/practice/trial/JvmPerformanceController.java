package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.trial.gc.JvmGcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RequestMapping("/jvm/performance")
@RestController
@Slf4j
public class JvmPerformanceController {

    @Autowired
    private JvmGcService jvmGcService;

    // 用于模拟内存泄漏的静态集合
    private static final Map<String, Object> LEAK_MAP = new ConcurrentHashMap<>();
    
    // 用于模拟线程阻塞的锁对象
    private static final Object LOCK1 = new Object();
    private static final Object LOCK2 = new Object();

    /**
     * 模拟堆内存溢出
     */
    @RequestMapping("/oom/heap")
    public Object oomHeap() {
        log.info("JvmPerformanceController oomHeap");
        List<byte[]> list = new ArrayList<>();
        try {
            while (true) {
                // 每次分配10MB
                byte[] largeObject = new byte[10 * 1024 * 1024];
                list.add(largeObject);
                Thread.sleep(100);
                log.info("Allocated: {} MB", list.size() * 10);
            }
        } catch (Exception e) {
            log.error("OOM occurred: {}", e.getMessage());
            return "OOM模拟完成: " + e.getMessage();
        }
    }

    /**
     * 模拟内存泄漏
     */
    @RequestMapping("/leak/memory")
    public Object memoryLeak(@RequestParam(value = "entries", defaultValue = "100000") int entries) {
        log.info("JvmPerformanceController memoryLeak entries = {}", entries);
        
        for (int i = 0; i < entries; i++) {
            // 创建大对象并放入静态集合
            byte[] largeObject = new byte[1024];
            LEAK_MAP.put("key-" + System.currentTimeMillis() + "-" + i, largeObject);
        }
        
        log.info("Memory leak simulation completed. LEAK_MAP size: {}", LEAK_MAP.size());
        return "内存泄漏模拟完成，已添加 " + entries + " 个对象到静态集合";
    }

    /**
     * 清理内存泄漏模拟数据
     */
    @RequestMapping("/leak/clean")
    public Object cleanMemoryLeak() {
        log.info("JvmPerformanceController cleanMemoryLeak");
        LEAK_MAP.clear();
        log.info("Memory leak simulation cleaned. LEAK_MAP size: {}", LEAK_MAP.size());
        return "内存泄漏模拟数据已清理";
    }

    /**
     * 模拟死锁
     */
    @RequestMapping("/deadlock")
    public Object deadlock() {
        log.info("JvmPerformanceController deadlock");
        
        Thread thread1 = new Thread(() -> {
            synchronized (LOCK1) {
                log.info("Thread 1 acquired LOCK1");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                log.info("Thread 1 trying to acquire LOCK2");
                synchronized (LOCK2) {
                    log.info("Thread 1 acquired LOCK2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (LOCK2) {
                log.info("Thread 2 acquired LOCK2");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                log.info("Thread 2 trying to acquire LOCK1");
                synchronized (LOCK1) {
                    log.info("Thread 2 acquired LOCK1");
                }
            }
        });

        thread1.start();
        thread2.start();
        
        log.info("Deadlock simulation started. Check thread dump for deadlock");
        return "死锁模拟已启动，请使用 jstack 查看线程堆栈";
    }

    /**
     * 模拟线程阻塞（活锁）
     */
    @RequestMapping("/livelock")
    public Object livelock() {
        log.info("JvmPerformanceController livelock");
        
        CountDownLatch latch = new CountDownLatch(2);
        
        Thread thread1 = new Thread(() -> {
            try {
                while (true) {
                    log.info("Thread 1 processing");
                    Thread.sleep(100);
                    if (Thread.interrupted()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                while (true) {
                    log.info("Thread 2 processing");
                    Thread.sleep(100);
                    if (Thread.interrupted()) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        thread1.start();
        thread2.start();
        
        // 30秒后中断线程
        new Thread(() -> {
            try {
                Thread.sleep(30000);
                thread1.interrupt();
                thread2.interrupt();
                latch.await(5, TimeUnit.SECONDS);
                log.info("Livelock simulation completed");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
        
        log.info("Livelock simulation started. Threads will run for 30 seconds");
        return "活锁模拟已启动，线程将运行30秒";
    }

    /**
     * 模拟CPU密集型任务
     */
    @RequestMapping("/cpu/intensive")
    public Object cpuIntensive(@RequestParam(value = "duration", defaultValue = "10") int duration) {
        log.info("JvmPerformanceController cpuIntensive duration = {}", duration);
        
        Thread thread = new Thread(() -> {
            long endTime = System.currentTimeMillis() + duration * 1000;
            while (System.currentTimeMillis() < endTime) {
                // 执行密集计算
                double result = 0;
                for (int i = 0; i < 1000000; i++) {
                    result += Math.sqrt(Math.random() * 10000);
                }
            }
            log.info("CPU intensive task completed");
        });
        
        thread.start();
        return "CPU密集型任务已启动，将运行 " + duration + " 秒";
    }

    /**
     * 模拟线程池饱和
     */
    @RequestMapping("/threadpool/saturation")
    public Object threadPoolSaturation(@RequestParam(value = "tasks", defaultValue = "100") int tasks) {
        log.info("JvmPerformanceController threadPoolSaturation tasks = {}", tasks);
        
        for (int i = 0; i < tasks; i++) {
            final int taskId = i;
            new Thread(() -> {
                try {
                    log.info("Task {} started", taskId);
                    // 模拟长时间运行的任务
                    Thread.sleep(5000);
                    log.info("Task {} completed", taskId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
        
        return "线程池饱和模拟已启动，已创建 " + tasks + " 个线程";
    }
}
