package com.bage.study.java11;

import java.util.Arrays;

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
