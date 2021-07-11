package com.bage.demo;

import java.util.Random;

public class HelloService {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("start....");
        HelloService helloService = new HelloService();
        Random random = new Random();
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