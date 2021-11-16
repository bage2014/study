package com.bage.jmockit;

import mockit.*;
import org.junit.Assert;
import org.junit.Test;

//HelloJMockit类的测试类
public class HelloServiceTest {

    @Tested
    HelloService helloService;
    @Injectable
    HelloJMockit helloJMockit;

    @Test
    public void test(final @Mocked HelloJMockit target) {
// 录制(Record)
        new Expectations() {
            {
                target.sayHello();
                // 期待上述调用的返回是"hello,david"，而不是返回"hello,JMockit"
                result = "hello,david";
            }
        };
        // 重放(Replay)
        String msg = helloService.sayHello();
        System.out.println(msg);
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
