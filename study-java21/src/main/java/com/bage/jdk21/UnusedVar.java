package com.bage.jdk21;

public class UnusedVar {

    public static void main(String[] args) {
        System.out.println(getVar("jjj"));
    }

    private static String getVar(String hello) {
        for (Character _ : hello.toCharArray()) {
            // _ 不使用这个对象
            System.out.println("hhhh");
        }
        return "hrllo";
    }
}
