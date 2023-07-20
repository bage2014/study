package com.bage.study.spring.boot;

import com.bage.study.springboot.async.AsyncService;
import com.bage.study.springboot.async.Starter;
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
    public void testAsync2() throws InterruptedException {
        String s = asyncService.async2();// 此方式为 异步调用
        System.out.println(s);
        Thread.sleep(3000L);
    }

    @Test
    public void testAsync1() throws InterruptedException {
        String async1 = asyncService.async1();
        System.out.println(async1);
        String async2 = asyncService.async2();
        System.out.println(async2);
        Thread.sleep(3000L);
    }
}