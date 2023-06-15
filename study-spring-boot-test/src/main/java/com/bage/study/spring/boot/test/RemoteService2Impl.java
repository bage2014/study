package com.bage.study.spring.boot.test;

import org.springframework.stereotype.Component;

@Component
public class RemoteService2Impl implements RemoteService2{
    @Override
    public String doSomething2() {
        System.out.println("RemoteServiceImpl2 is work");
        return "RemoteServiceImpl2 is work";
    }
}
