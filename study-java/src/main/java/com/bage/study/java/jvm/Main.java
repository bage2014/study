package com.bage.study.java.jvm;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        Runtime runtime = Runtime.getRuntime();

        System.out.println("maxMemory:" + runtime.maxMemory() / 1024 / 1024 + " MB");
        System.out.println("freeMemory:" + runtime.freeMemory() / 1024 / 1024 + " MB");
        System.out.println("totalMemory:" + runtime.totalMemory() / 1024 / 1024 + " MB");

//        List<Object> listObject = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            byte[] b = new byte[1024 * 1024];
            System.out.println(i + "-----------------");
            Thread.sleep(1000);
//            listObject.add(b);
        }

    }

}
