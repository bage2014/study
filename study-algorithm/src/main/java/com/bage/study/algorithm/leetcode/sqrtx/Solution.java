package com.bage.study.algorithm.leetcode.sqrtx;

/**
 * https://leetcode.cn/problems/sqrtx/description/
 */
class Solution {
    public int mySqrt(int x) {
        for (int i = 0; i < x; i++) {
            int left = i * i;
            if (left == x) {
                return i;
            }
            int right = (i + 1) * (i + 1);
            if (right == x) {
                return i + 1;
            }

            if (i * i < x && (i + 1) * (i + 1) > x) {
                return i;
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().mySqrt(4));
        System.out.println(new Solution().mySqrt(8));
        // 二分查找，用x/m<m而不是m*m>x防止溢出 ?
        System.out.println(new Solution().mySqrt(2147483647)); // todo bage 超出了INT 范围
    }
}