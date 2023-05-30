package com.bage.study.springboot.condition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;

@SpringBootApplication
public class ConditionApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConditionApplication.class, args);
    }

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));
    }
}