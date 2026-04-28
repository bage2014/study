package com.bage.study.ai.best.practice.exception.analysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;

@SpringBootApplication(exclude = {OpenAiAutoConfiguration.class})
public class ExceptionAnalysisApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExceptionAnalysisApplication.class, args);
    }
}
