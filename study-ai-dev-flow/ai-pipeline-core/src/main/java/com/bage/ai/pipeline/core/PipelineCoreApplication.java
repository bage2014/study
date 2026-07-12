package com.bage.ai.pipeline.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.bage.ai.pipeline.core", "com.bage.ai.pipeline.api.controller", "com.bage.ai.pipeline.agent"})
@EntityScan(basePackages = {"com.bage.ai.pipeline.api.entity"})
@EnableJpaRepositories(basePackages = {"com.bage.ai.pipeline.api.repository"})
public class PipelineCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PipelineCoreApplication.class, args);
    }
}
