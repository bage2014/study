package com.bage.study.springboot.aop.annotation.flow.copy;

import org.springframework.stereotype.Component;

@Component
public class MyHelloService implements HelloService {

    @Override
    @FlowCopy(toClass = YouHelloService.class)
    public String sayHi(String param) {
        System.out.println("MyHelloService.sayHi:" + param);
        return "MyHelloService.sayHi:" + param;
    }

    @Override
    @FlowCopy(toClass = YouHelloService.class)
    public String sayHey(String param) {
        System.out.println("MyHelloService.sayHey:" + param);
        return "MyHelloService.sayHey:" + param;
    }

    @Override
    public String sayYo(String param) {
        System.out.println("MyHelloService.sayYo:" + param);
        return "MyHelloService.sayYo:" + param;
    }

}
