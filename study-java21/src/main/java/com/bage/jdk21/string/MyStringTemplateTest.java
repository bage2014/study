package com.bage.jdk21.string;

public class MyStringTemplateTest {
    public static void main(String[] args) {
        String world = "hello";
        String helloWorld1 = world + " world";
        System.out.println(helloWorld1);

        // 字符串拼接
        Object helloWorld2 = StringTemplate.STR."hello \{ world }" ;
        System.out.println(helloWorld2);

        boolean isOk = false; // 支持表达式
        Object helloWorld3 = StringTemplate.STR."hello \{ isOk ? "ok" : "notOk" }" ;
        System.out.println(helloWorld3);
    }
}
