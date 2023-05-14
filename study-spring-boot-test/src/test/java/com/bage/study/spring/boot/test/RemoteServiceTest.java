package com.bage.study.spring.boot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteServiceTest {

    @Autowired
    private RemoteService remoteService;

    @Test
    public void mockRemote() throws Exception {
        System.out.println(remoteService.doSomething());
    }

}