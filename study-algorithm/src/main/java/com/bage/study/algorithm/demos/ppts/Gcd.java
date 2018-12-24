package com.bage.study.algorithm.demos.ppts;

/**
 * 求两个正整数m,n的最大公因子
 */
public class Gcd {

    public static void main(String[] args) {
        int m = 20;
        int n = 25;

        int res = work(m,n);
        System.out.println(res);
    }

    /**
     * 求两个正整数m,n的最大公因子
     * @param m
     * @param n
     * @return
     */
    private static int work(int m, int n) {
        int n1 = gcdForce(m,n); // 暴力法求解
        int n2 = gcd(m,n); // 辗转相除法求解
        return n1 == n2 ? n1 : -1;
    }

    /**
     * 输入:正整数m和n
     * 输出:m和n的最大公因子
     * 第一步	：如果n=0，返回m的值作为结果，同时过程结束，否则转入第二步
     * 第二步	：r=m%n
     * 第三步	：m=n,n=r, 返回第一步
     * @param m
     * @param n
     * @return
     */
    private static int gcd(int m, int n) {

        int temp = 0;
        if(m < n){ // 保证 m >= n
            temp = m;
            m = n;
            n = temp;
        }
        if(n == 0){
            return m;
        }
        int r = 0;
        while(n > 0){
            r = m % n;
            if(r == 0){
                return n;
            }
            m = n;
            n = r;
        }
        return 1;
    }

    /**
     * 暴力法求解
     * @param m
     * @param n
     * @return
     */
    public static int gcdForce(int m, int n) {
        int min = m > n ? n : m; // 取较小值
        for (int i = min;i >= 2; i --){
            if(m % i == 0 && n % i == 0){
                return i;
            }
        }
        return 1;
    }
}
