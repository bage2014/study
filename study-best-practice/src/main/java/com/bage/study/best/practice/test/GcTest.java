package com.bage.study.best.practice.test;

import java.util.ArrayList;
import java.util.List;

public class GcTest {

    private final static int oneM = 1024 * 1024;

    public static void main(String[] args) {

        int n = 10000;
        List<Object> temps = new ArrayList();
        for (int i = 0; i < n; i++) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            temps.add(new byte[oneM * 10]);


        }
    }

}
