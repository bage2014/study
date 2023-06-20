package com.bage.study.spring.boot.test.mock;

import com.bage.study.spring.boot.test.RemoteService3;

public class RemoteService3Mock implements RemoteService3 {
    @Override
    public String doSomething3() {
        System.out.println("RemoteService3Mock is work");
        return "RemoteService3Mock is work";
    }
}
