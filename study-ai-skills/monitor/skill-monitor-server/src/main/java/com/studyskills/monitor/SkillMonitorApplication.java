package com.studyskills.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SkillMonitorApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkillMonitorApplication.class, args);
    }
}
