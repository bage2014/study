package com.bage.study.spring.boot.test.mock;

import com.bage.study.spring.boot.test.RemoteService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class RemoteServiceMock implements RemoteService {
    @Override
    public String doSomething() {
        System.out.println("RemoteServiceMock is work");
        return "RemoteServiceMock is work";
    }
}
