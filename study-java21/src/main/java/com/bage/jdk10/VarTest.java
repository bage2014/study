package com.bage.jdk10;

import java.util.ArrayList;

public class VarTest {
    public static void main(String[] args) {
        var str = "Hello, World!"; // 推断为 String 类型
        var list = new ArrayList<String>(); // 推断为 ArrayList<String> 类型

        System.out.println(str);

        String hhh = str;
        System.out.println(hhh);

        list.add(str);
        System.out.println(list);



    }
}
