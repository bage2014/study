package com.bage.study.springboot.enable.starter;

import com.bage.study.springboot.enable.EnableMyService;
import com.bage.study.springboot.enable.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.bage.study.springboot.enable.starter")
@EnableMyService
public class MyEnableApplication implements CommandLineRunner {

    @Autowired
    MyService myService;

    public static void main(String[] args) {
        SpringApplication.run(MyEnableApplication.class, args);
        
    }

    public void run(String... args) throws Exception {
        System.out.println(myService.hello(" bage"));
    }
}