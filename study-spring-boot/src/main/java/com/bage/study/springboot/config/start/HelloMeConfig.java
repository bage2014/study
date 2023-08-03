package com.bage.study.springboot.config.start;

import com.bage.study.springboot.config.config.HelloWorldService;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloMeConfig {

    @Bean
    public HelloWorldService helloService3() {
        System.out.println("HelloMeConfig");
        return new HelloWorldService();
    }

}
