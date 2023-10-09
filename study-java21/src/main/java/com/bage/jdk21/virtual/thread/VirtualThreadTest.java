package com.bage.jdk21.virtual.thread;

public class VirtualThreadTest {
    public static void main(String[] args) throws InterruptedException {

        int n = 10000;
        boolean isLogger = false;
        newNThread(n,isLogger);

        newVirtualThread(n,isLogger);

        Thread.sleep(1000 * 10);
    }

    private static void newVirtualThread(int n, boolean isLogger) {
        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            Thread.ofPlatform().name("newVirtualThread-test-" + i).start(new SimpleThread(isLogger));
        }
        System.out.println("newVirtualThread time-cost:" + (System.currentTimeMillis() - currentTimeMillis));

    }

    private static void newNThread(int n, boolean isLogger) {

        long currentTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < n; i++) {
            final int nnn = i;
            new Thread(){
                @Override
                public void run() {
                    if(!isLogger){
                        return ;
                    }
                    System.out.println("nnn+" + nnn);
                }
            }.start();
        }
        System.out.println("newNThread time-cost:" + (System.currentTimeMillis() - currentTimeMillis));


    }
}
