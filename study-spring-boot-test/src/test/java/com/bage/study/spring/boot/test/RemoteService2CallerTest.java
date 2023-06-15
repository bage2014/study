package com.bage.study.spring.boot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteService2CallerTest {

    @Autowired
    private RemoteService2Caller remoteService2Caller;

    @Test
    public void mockRemote() {
        System.out.println(remoteService2Caller.doSomething());
    }


}

