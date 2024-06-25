package com.bage.study.springboot.ann;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyRequestParamApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MyRequestParamApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // http://localhost:8080/query/page?current=1&size=12
        // 自动包装成 PageRequestParam 对象
    }

}