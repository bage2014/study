package com.bage.study.agent.demo.service;

import java.util.Random;

public class HelloService {

    public static void main(String[] args) throws InterruptedException {
        HelloService helloService = new HelloService();
        Random random = new Random();
        System.out.println("HelloService started....");

        while (true) {
            Thread.sleep(5000L);
            String result = helloService.sayHi(String.valueOf(random.nextInt(10000)));
            System.out.println(result);
        }
    }

    public String sayHi(String msg) {
        System.out.println("sayHi: " + msg);
        return "result: " + msg;
    }

}