package com.bage.study.algorithm.leetcode.reverseinteger;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/reverse-integer/
 *
 * 给你一个 32 位的有符号整数 x ，返回将 x 中的数字部分反转后的结果。
 *
 * 如果反转后整数超过 32 位的有符号整数的范围 [−231,  231 − 1] ，就返回 0。
 *
 * 假设环境不允许存储 64 位整数（有符号或无符号）。
 *
 */
class Solution {
    // todo bage 边界判断？？
    public int reverse(int num) {
        if (num == 0) {
            return 0;
        }

        boolean negative = false;
        if (num < 0) {
            // 负数
            num = 0 - num;
            negative = true;
        }

        // 全部正数方式处理
        List<Integer> list = splitList(num);

        int result = 0;
        for (int i = 0; i < list.size(); i++) {
            Double v = list.get(i) * Math.pow(10, list.size() - 1 - i);
            result += v.intValue();
        }
        return negative ? (0 - result) : result;
    }

    private List<Integer> splitList(int num) {
        int remain = 0;

        List<Integer> list = new ArrayList<>();
        while (true) {
            remain = num % 10;
            num = num / 10;

            if (remain == 0 && list.isEmpty()) {
                continue;
            }
            list.add(remain);
            if (num <= 0) {
                break;
            }
        }
        return list;
    }


    public static void main(String[] args) {
        System.out.println(new Solution().reverse(982));
        System.out.println(new Solution().reverse(123));
        System.out.println(new Solution().reverse(-123));
        System.out.println(new Solution().reverse(120));
        System.out.println(new Solution().reverse(0));
    }


}