package com.bage.study.spring.boot.test;

import com.bage.study.spring.boot.test.mock.RemoteService3Mock;
import org.junit.Before;
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
    RemoteService3 mock = new RemoteService3Mock();

    @Before
    public void init(){
        when(remoteService3.doSomething3()).thenReturn(mock.doSomething3());
    }

    @Test
    public void mockRemote() {
//        when(remoteService3.doSomething3()).thenReturn("doSomething3 Mock");
//        when(remoteService3.doSomething3()).thenReturn(mock.doSomething3());
        System.out.println(remoteService3Caller.doSomething());
    }


}

