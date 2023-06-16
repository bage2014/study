package com.bage.study.spring.boot.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteService3CallerTest {

    @Autowired
    private RemoteService3Caller remoteService3Caller;
    @MockBean
    private RemoteService3 remoteService3;

//    @Before
//    public void init(){
//        when(remoteService3.doSomething3()).thenReturn("doSomething3 Mock");
//    }

    @Test
    public void mockRemote() {
        when(remoteService3.doSomething3()).thenReturn("doSomething3 Mock");
        System.out.println(remoteService3Caller.doSomething());
    }


}

