package com.bage.study.jmeter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = "com.bage.study.jmeter.repo")
public class JMeterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JMeterApplication.class, args);
    }
}
