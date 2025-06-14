package com.bage.study.springboot.aop.annotation.gray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class YouHelloService implements HelloService {

    @Autowired
    private TheirHelloService theirHelloService;

    @Override
    @OrderGrayFlow(toClass = MyHelloService.class, toMethod = "sayHi")
    public String sayHi(String param) {
        System.out.println("YouHelloService.sayHi:" + param);
        return "YouHelloService.sayHi:" + param;
    }

    @Override
    public String sayHey(String param) {
        System.out.println("YouHelloService.sasayHeyyHi:" + param);
        return "YouHelloService.sayHey:" + param;
    }

    @Override
    public String sayYo(String param) {
        System.out.println("YouHelloService.sayYo:" + param);
        return "YouHelloService.sayYo:" + param;
    }

}
