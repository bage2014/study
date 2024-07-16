package com.bage.study.jmh;

import java.util.Random;

public class SortCompare {
    public static void main(String[] args) {
        int[] data = initData(10000);

        long start = System.currentTimeMillis();

        long end = System.currentTimeMillis();
        System.out.println("time cost = " + (end - start));


        System.out.println("time cost = " + (end - start));

    }

    private static int[] initData(int n) {
        Random random = new Random();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = random.nextInt(10000);
        }
        return arr;
    }
}
