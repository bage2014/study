package com.bage.study.dubbo.spring.boot.provider;

import com.bage.study.dubbo.spring.boot.api.HelloParam;
import com.bage.study.dubbo.spring.boot.api.HelloResult;
import com.bage.study.dubbo.spring.boot.api.HelloService;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.Date;
import java.util.Random;

@DubboService
public class HelloServiceImpl implements HelloService {
    private Random random = new Random();

    @Override
    public HelloResult sayHello(HelloParam param) {
        System.out.println(new Date() + "Get param ======> " + param);
        HelloResult result = new HelloResult();
        result.setCode(random.nextInt(1000));
        result.setMessage("OKKK");
        System.out.println(new Date() + "return result ======> " + result);
        return result;
    }
}
