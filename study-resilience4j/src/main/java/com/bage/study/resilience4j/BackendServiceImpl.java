package com.bage.study.resilience4j;

import java.util.Random;

public class BackendServiceImpl implements BackendService{

    Random random = new Random();
    @Override
    public String doSomething()  {
        if(random.nextInt(100) > 30){
            throw new RuntimeException("random.nextInt(100) > 30");
        }
        return "BackendServiceImpl doSomething is work";
    }
}
