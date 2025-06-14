package com.bage.study.springboot.aop.annotation.gray;

import org.springframework.stereotype.Component;

@Component
public class TheirHelloService implements HelloService {

    @Override
    @OrderGrayFlow(toClass = MyHelloService.class, toMethod = "sayHi")
    public String sayHi(String param) {
        System.out.println("TheirHelloService.sayHi:" + param);
        return "TheirHelloService.sayHi:" + param;
    }

    @Override
    public String sayHey(String param) {
        System.out.println("TheirHelloService.sasayHeyyHi:" + param);
        return "TheirHelloService.sayHey:" + param;
    }

    @Override
    public String sayYo(String param) {
        System.out.println("TheirHelloService.sayYo:" + param);
        return "TheirHelloService.sayYo:" + param;
    }

}
