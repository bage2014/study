package com.bage.study.springboot.aop.annotation.flow.copy;

import org.springframework.stereotype.Component;

@Component
public class TheirHelloService implements HelloService {

    @Override
    @FlowCopy(toClass = MyHelloService.class, toMethod = "sayHi")
    public String sayHi(String param) {
        System.out.println("TheirHelloService.sayHi:" + param);
        return "TheirHelloService.sayHi:" + param;
    }

    @Override
    public String sayHey(String param) {
        System.out.println("YouHelloService.sasayHeyyHi:" + param);
        return "YouHelloService.sayHey:" + param;
    }

}
