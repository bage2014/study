package com.bage.study.best.practice.trial;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://zhuanlan.zhihu.com/p/286110609
 *
 *
 * -XX:+PrintSafepointStatistics  等待1分钟停止程序
 *
 * -XX:+PrintGCDetails 每次GC发生时打印GC详细信息
 *
 *
 *
 */
@RequestMapping("/gc/safe/point")
@RestController
@Slf4j
public class GcSafePointController {
    static Thread t1 = new Thread(() -> {
        while (true) {
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

    static Thread t2 = new Thread(() -> {
        while (true) {
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

    private static final int _50KB = 50 * 1024;

    static Thread t3 = new Thread(() -> {
        while (true) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            byte[] bytes = new byte[_50KB];
        }
    });

    @RequestMapping("/start")
    public Object start(@RequestParam(value = "count", required = false) Long count) throws InterruptedException {
        t1.start();
        Thread.sleep(5000L);
        System.out.println("t2 t3 starting...");
        t2.start();
        t3.start();
        return "start";
    }

    public static void main(String[] args) throws InterruptedException {
        t1.start();
        Thread.sleep(5000L);
        System.out.println("t2 t3 starting...");
        t2.start();
        t3.start();
    }

}
