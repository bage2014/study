package com.bage.study.java.loader;

public class ClassLoadInstance {

    static {
        System.out.println("ClassLoadInstance类初始化时就会被执行！");
    }

    public ClassLoadInstance() {
      System.out.println("ClassLoadInstance构造函数！");
    }
}
