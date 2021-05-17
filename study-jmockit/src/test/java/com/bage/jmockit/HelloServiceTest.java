package com.bage.jmockit;

import mockit.Expectations;
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
// 录制(Record)
        new Expectations() {
            {
                helloJMockit.sayHello();
                // 期待上述调用的返回是"hello,david"，而不是返回"hello,JMockit"
                result = "hello,david";
            }
        };
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
