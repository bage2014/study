package com.bage.study.springboot.aop.order;

import org.springframework.stereotype.Component;

@Component
public class HelloAopOrderService {

    public String hello(String msg) throws Exception {
        throw new Exception("hello");
//        return "hello," + msg;
    }

}
