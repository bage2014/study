package com.bage.study.spring.boot.test;

import org.springframework.stereotype.Component;

@Component
public class RemoteService3Impl implements RemoteService3 {

    @Override
    public String doSomething3() {
        System.out.println("RemoteServiceImpl3 is work");
        return "RemoteServiceImpl3 is work";
    }
}
