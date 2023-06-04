package com.bage.study.java11;

import java.util.Arrays;

/**
 * 只能在方法中、不能在类中
 */
public class VarTest {
//    var hh = "hhhh"; 不能在类中

    public static void main(String[] args) {
        var hh = "hh"; // 只能在方法中、不能在类中
        System.out.println(hh);

        var list = Arrays.asList("1","2");
        for (String item : list) {
            System.out.println(item);
        }

//        var hhh2; // 定义时候需要初始化
//        hhh2 = "";
        var hhh2 = "";
        System.out.println(hhh2);


    }

}
