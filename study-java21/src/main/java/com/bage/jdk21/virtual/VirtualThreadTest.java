package com.bage.jdk21.virtual;

public class VirtualThreadTest {
    public static void main(String[] args) {
        Thread.ofPlatform().name("thread-test").start(new SimpleThread());
    }
}
