package com.bage.jmockit;

//一个简单的类，能用不同语言打招呼
public class HelloService {
    private HelloJMockit helloJMockit = new HelloJMockit();

    public String sayHello() {
        return "HelloService:" + helloJMockit.sayHello();
    }

}