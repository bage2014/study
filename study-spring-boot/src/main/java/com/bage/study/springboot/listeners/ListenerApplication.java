package com.bage.study.springboot.listeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ListenerApplication {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
//        ConfigurableApplicationContext run = SpringApplication.run(ListenerApplication.class, args);
        SpringApplication springApplication = new SpringApplication(ListenerApplication.class);
        springApplication.addListeners(new MySpringEventListener());
        springApplication.run(args);
        long end = System.currentTimeMillis();
        System.out.println("timeCost=" + (end - start));
    }

}