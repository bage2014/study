package com.bage.study.springboot.condition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfig {

    @Bean
    @Conditional(value = PropertiesCondition.class)
    public HelloService helloService(){
        return new HelloServiceImpl1();
    }

}
