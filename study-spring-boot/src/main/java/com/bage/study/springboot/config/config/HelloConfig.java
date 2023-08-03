package com.bage.study.springboot.config.config;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureOrder(-10)
public class HelloConfig {

    @Bean
    @ConditionalOnMissingBean(name = "helloService3")
    public HelloWorldService helloService() {
        System.out.println("HelloConfig");
        return new HelloWorldService();
    }

}
