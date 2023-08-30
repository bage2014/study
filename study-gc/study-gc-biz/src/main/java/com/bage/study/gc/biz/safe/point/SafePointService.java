package com.bage.study.gc.biz.safe.point;

import lombok.extern.slf4j.Slf4j;

/**
 * https://zhuanlan.zhihu.com/p/286110609
 * <p>
 * <p>
 * -XX:+PrintSafepointStatistics  等待1分钟停止程序
 * <p>
 * -XX:+PrintGCDetails 每次GC发生时打印GC详细信息
 */
@Slf4j
public class SafePointService {
    private static final int _50KB = 50 * 1024;
    private boolean canRunning = false;

    Thread t1 = new Thread(() -> {
        while (canRunning) {
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long cost = System.currentTimeMillis() - start;
            (cost > 1010L ? System.err : System.out).printf("thread: %s, costs %d ms\n", Thread.currentThread().getName(), cost);
        }
    });

    Thread t2 = new Thread(() -> {
        while (canRunning) {
            for (int i = 1; i <= 1000000000; i++) {
                boolean b = 1.0 / i == 0;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });


    Thread t3 = new Thread(() -> {
        while (canRunning) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] bytes = new byte[_50KB];
        }
    });

    public String start() throws Exception {
        canRunning = true;
        log.info("t2 t3 starting...");
        t1.start();
        Thread.sleep(5000L);
        log.info("t2 t3 starting...");
        t2.start();
        t3.start();
        return "start";
    }
    public String stop() throws Exception {
        log.info("stopping...");
        canRunning = false;
        return "stop";
    }

}
