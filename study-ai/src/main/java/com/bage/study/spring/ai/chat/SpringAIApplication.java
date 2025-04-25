package com.bage.study.spring.ai.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@Slf4j
@EnableAsync
public class SpringAIApplication implements CommandLineRunner {

    public static void main(String args[]) {
        SpringApplication.run(SpringAIApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}