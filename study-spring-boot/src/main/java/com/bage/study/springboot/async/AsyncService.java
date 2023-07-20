package com.bage.study.springboot.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncService {

    public String async1() {
        System.out.println("async1 currentThread： " + Thread.currentThread().getName());
        return "async1";
    }
    webflowContext
    @Async
    public String async2() {
        System.out.println("async2 currentThread： " + Thread.currentThread().getName());
        System.out.println("2:" + Thread.currentThread().getName());
        return "async2";
    }

}