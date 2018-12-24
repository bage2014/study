package com.bage.study.algorithm.algorithm.force;

/**
 * 求两个正整数m,n的最大公因子
 */
public class Gcd {

    public static void main(String[] args) {
        int m = 20;
        int n = 30;

        int res = com.bage.study.algorithm.demos.ppts.Gcd.gcdForce(m,n);
        System.out.println(res);

    }
}
