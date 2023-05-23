package com.bage.starter.hello;

import com.bage.starter.enable.EnableMyService;
import com.bage.starter.enable.MyService;
import com.bage.starter.ping.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMyService
public class MyStarterApplication implements CommandLineRunner {

    @Autowired
    PingService pingService;
    @Autowired
    MyService myService;

    public static void main(String[] args) {
        SpringApplication.run(MyStarterApplication.class, args);
        
    }

    public void run(String... args) throws Exception {
        System.out.println(pingService);

        System.out.println(myService.hello(" bage"));
    }
}