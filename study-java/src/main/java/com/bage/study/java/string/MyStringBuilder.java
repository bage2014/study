 package com.bage.study.java.string;

public class MyStringBuilder {

    public static void main(String[] args) {

        String str1 = new StringBuilder().append("ja").append("va").toString();
        System.out.println(str1.intern() == str1); // false

        String str2 = new StringBuilder("ja").append("va").toString();
        System.out.println(str2.intern() == str2); //false

        String str3 = new StringBuilder("计算机").append("软件").toString();
        System.out.println(str3.intern() == str3); // true

    }

}
