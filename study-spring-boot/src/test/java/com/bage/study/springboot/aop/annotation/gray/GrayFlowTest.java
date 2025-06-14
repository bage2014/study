package com.bage.study.springboot.aop.annotation.gray;


import com.bage.study.springboot.aop.annotation.flow.copy.FlowCopyApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = GrayFlowApplication.class)
public class GrayFlowTest {

    @Autowired
    MyHelloService helloService;

    @Test
    public void sayHi() {
        helloService.sayHi("hihi");
    }

    @Test
    public void sayHiHey() {
        helloService.sayHi("hihi");
        helloService.sayHey("heihie");
    }
}
