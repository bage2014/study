package com.bage.study.pulsar;

public class MyPulsar {
    public static void main(String[] args) throws InterruptedException {
        //  start MyConsumer
//        MyConsumer myConsumer = new MyConsumer();
//        myConsumer.listener();

        MyProducer myProducer = new MyProducer();
        myProducer.send("hello-bage");

        Thread.sleep(1000*2);
//        myConsumer.close();
    }
}
