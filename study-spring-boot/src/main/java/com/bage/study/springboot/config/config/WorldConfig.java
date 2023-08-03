package com.bage.study.springboot.config.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(HelloConfig.class)
@AutoConfigureOrder(-456)
public class WorldConfig {

    @Bean
    public HelloWorldService helloService2() {
        System.out.println("WorldConfig");
        return new HelloWorldService();
    }

}
