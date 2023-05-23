package com.bage.starter.enable;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(MyService.class)
public class MyServiceConfig {

    @Bean
    MyService myService() {
        return new MyService();
    }

}
