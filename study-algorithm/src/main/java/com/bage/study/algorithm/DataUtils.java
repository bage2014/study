package com.bage.study.algorithm;

import java.util.Random;

public class DataUtils {

    public static void main(String[] args) {
        int[] a = DataUtils.init(9);
        print(a);
    }

    public static void print(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " \t");
        }
        System.out.println();
    }

    public static int[] init(int n) {
        int a[] = new int[n];
        for (int i = 0; i < a.length; i++) {
            a[i] = i+ 1;
        }
        Random random = new Random();
        int randomI = 0;
        int temp = 0;
        for (int i = 0; i < a.length; i++) {
            randomI = random.nextInt(n - i);
            temp = a[randomI];
            a[randomI] = a[i];
            a[i] = temp;
        }
        return a;
    }
}
