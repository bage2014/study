package com.bage;

import com.bage.study.mockito.RemoteService;
import com.bage.study.mockito.RemoteServiceCaller;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteServiceCallerTest {

    @Autowired
    private RemoteServiceCaller remoteServiceCaller;
    @MockBean
    private RemoteService remoteService3;

    @Test
    public void testRemote() {
        mockRemoteService();
        System.out.println(remoteServiceCaller.doSomething());
    }

    private void mockRemoteService() {
        when(remoteService3.doSomething()).thenReturn("doSomething Mock");
    }


}

