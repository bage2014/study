package com.bage.study.spring.boot.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoteService3Caller {
    @Autowired
    private RemoteService3 remoteService3;

    public String doSomething() {
        return remoteService3.doSomething3();
    }
}
