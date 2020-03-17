package com.bage;

import com.bage.starter.ping.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    PingService pingService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        
    }

    public void run(String... args) throws Exception {
        System.out.println(pingService);
    }
}