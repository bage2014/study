package com.bage.agent;

public class HelloService {

    public String sayHi(String msg) {
        System.out.println("HelloService seyHi " + msg);
        return "result: HelloService seyHi ";
    }

}