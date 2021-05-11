package com.bage.jmockit;

import mockit.Mocked;
import mockit.Verifications;
import org.junit.Assert;
import org.junit.Test;

//HelloJMockit类的测试类
public class HelloServiceTest {

    @Mocked
    HelloService helloService;
    @Mocked
    HelloJMockit helloJMockit;

    @Test
    public void test() {

        // 重放(Replay)
        String msg = helloService.sayHello();
        Assert.assertTrue(msg.equals("hello,david"));
        // 验证(Verification)
        new Verifications() {
            {
                helloService.sayHello();
                times = 1;
            }
        };
    }

}
