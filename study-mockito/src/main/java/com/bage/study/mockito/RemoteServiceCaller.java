package com.bage.study.mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoteServiceCaller {
    @Autowired
    private RemoteService remoteService;

    public String doSomething() {
        return remoteService.doSomething();
    }
}
