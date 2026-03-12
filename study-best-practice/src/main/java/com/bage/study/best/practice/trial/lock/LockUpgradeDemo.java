package com.bage.study.best.practice.trial.lock;

public class LockUpgradeDemo {
    private static final Object lock = new Object();
    private static int counter = 0;
    
    public static void main(String[] args) {
        // 创建多个线程竞争锁
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100000; j++) {
                    synchronized (lock) {
                        counter++;
                    }
                }
            }).start();
        }
        
        // 等待所有线程执行完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Counter: " + counter);
    }
}