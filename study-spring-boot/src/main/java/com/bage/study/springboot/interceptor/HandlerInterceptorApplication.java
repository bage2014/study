package com.bage.study.springboot.interceptor;

import com.bage.study.springboot.aop.annotation.rest.RestResult;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class HandlerInterceptorApplication {

    public static void main(String[] args) {
        SpringApplication.run(HandlerInterceptorApplication.class, args);
    }

    @RestResult
    @RequestMapping("/hello")
    public Object hello(){
        return "hello";
    }

}