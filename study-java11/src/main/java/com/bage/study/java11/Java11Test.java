package com.bage.study.java11;

import java.util.Arrays;

/**
 * 1. 支持源码直接运行
 * 2. xxxx
 *
 * https://openjdk.java.net/projects/jdk/12/
 * https://openjdk.java.net/projects/jdk/13/
 * https://openjdk.java.net/projects/jdk/14/
 * https://openjdk.java.net/projects/jdk/15/
 * https://openjdk.java.net/projects/jdk/16/
 * https://openjdk.java.net/projects/jdk/17/
 */
public class Java11Test {

    public static void main(String[] args) {
        var hh = "hh";
        System.out.println(hh);

        var list = Arrays.asList("1","2");
        for (String item : list) {
            System.out.println(item);
        }

    }

}
