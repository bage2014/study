package com.bage.study.spring.boot.test.mock;

import com.bage.study.spring.boot.test.RemoteService2;

public class RemoteServiceMock2 implements RemoteService2 {
    @Override
    public String doSomething2() {
        System.out.println("RemoteServiceMock2 is work");
        return "RemoteServiceMock2 is work";
    }
}
