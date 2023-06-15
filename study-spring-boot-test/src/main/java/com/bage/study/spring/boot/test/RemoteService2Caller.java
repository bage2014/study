package com.bage.study.spring.boot.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoteService2Caller {
    @Autowired
    private RemoteService2 remoteService2;

    public String doSomething() {
        return remoteService2.doSomething2();
    }
}
