package com.bage.study.spring.boot;

import com.bage.study.springboot.aop.AsyncService;
import com.bage.study.springboot.aop.Starter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) 
@SpringBootTest(classes = Starter.class)
public class BaseTest {

    @Autowired
    AsyncService asyncService;

    @Test
    public void testAsync2() {
        asyncService.async2(); // 此方式为 异步调用
    }

    @Test
    public void testAsync1() {
        asyncService.async1();
    }
}