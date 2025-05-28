package com.bage.study.springboot.aop.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AopOrderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(AopOrderApplication.class, args);
        long end = System.currentTimeMillis();
        System.out.println("timeCost=" + (end-start));
    }


    @Autowired
    HelloAopOrderService helloAopOrderService;


    @Override
    public void run(String... args) throws Exception {
        helloAopOrderService.hello("world");
        System.out.println("------hello-end-------");
        helloAopOrderService.hello2("world");
        System.out.println("------hello2-end-------");
        helloAopOrderService.hello3("world");
        System.out.println("------hello3-end-------");
    }

}