package com.bage;


public class HelloJdk21 {
    public static void main(String[] args) {
        int n = 100;
        int sum = 0;
        for (int i = 1; i <= n; i++) {

            sum += i;
        }
        System.out.println(sum);
    }
}
