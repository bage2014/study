package com.bage.jdk21.virtual.thread;

public class SimpleThread implements Runnable{

    boolean isLogger = false;

    public SimpleThread(boolean isLogger) {
        this.isLogger = isLogger;
    }

    @Override
    public void run() {
        if(!isLogger){
            return;
        }
        System.out.println("SimpleThread-bage-hhhh-");
    }
}
