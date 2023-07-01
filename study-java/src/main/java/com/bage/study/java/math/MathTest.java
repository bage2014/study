package com.bage.study.java.math;

public class MathTest {

    public static void main(String[] args) {
        // 四舍五入
        long round = Math.round(-1.5);
        System.out.println(round);

        // 向上取整数
        System.out.println(Math.ceil(123.34));

        // 向下取整数
        System.out.println(Math.floor(123.34));

        // 取最大值
        System.out.println(Math.max(666,777));

        // 绝对值
        System.out.println(Math.abs(-797));

        // 立方根
        System.out.println(Math.cbrt(8));


    }

}
