package com.bage.study.java.multhread;

public class RunnableDemo {

    public static void main(String[] args) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Runnable hello world");
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

}
