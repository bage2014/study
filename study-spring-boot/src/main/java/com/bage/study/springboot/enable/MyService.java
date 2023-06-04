package com.bage.study.springboot.enable;

public class MyService {

    public String hello(String msg) {
        return String.format("MyService is work , msg = %s", msg);
    }
}
