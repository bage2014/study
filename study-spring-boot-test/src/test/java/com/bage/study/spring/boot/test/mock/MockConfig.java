package com.bage.study.spring.boot.test.mock;

import com.bage.study.spring.boot.test.RemoteService2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MockConfig {
    @Bean
    @Primary
    public RemoteService2 remoteServiceMock2() {
        System.out.println("remoteServiceMock2 config is work");
        return new RemoteServiceMock2();
    }
}