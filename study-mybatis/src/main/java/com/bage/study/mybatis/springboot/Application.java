package com.bage.study.mybatis.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses=Application.class)
@EnableCaching
public class Application{

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
