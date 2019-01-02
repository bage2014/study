package com.bage.study.java.attr;

public class AttrTest {

    public static void main(String[] args){

        SuperClass superClass = new SubClass();

        System.out.println(superClass.attr);
        System.out.println(superClass.staticAttr);

        System.out.println(superClass.method()); // 只有这个能重载
        System.out.println(superClass.staticMethod());

        /* 执行结果：
        SuperClass.attr
        SuperClass.staticAttr
        SubClass.method
        SuperClass.staticMethod
        */

    }

}
