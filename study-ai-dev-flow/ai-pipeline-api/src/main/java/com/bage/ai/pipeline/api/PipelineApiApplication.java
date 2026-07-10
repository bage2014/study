package com.bage.ai.pipeline.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.bage.ai.pipeline"})
public class PipelineApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PipelineApiApplication.class, args);
    }
}