package com.bage.study.algorithm.top100;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/reverse-integer/
 */
public class ReverseInteger {

    public static void main(String[] args) {
        System.out.println(new ReverseInteger().reverse(-2147483648));
    }
    public int reverse(int num) {
        if (num == 0) {
            return 0;
        }

        boolean negative = false;
        if (num < 0) {
            // 负数
            num = 0 - num;
            if(num < 0){
                return 0;
            }
            negative = true;
        }

        // 全部正数方式处理
        List<Integer> list = splitList(num);
        long result = 0;
        for (int i = 0; i < list.size(); i++) {
            Double v = list.get(i) * Math.pow(10, list.size() - 1 - i);
            result += v.intValue();
        }

        long r = negative ? (0 - result) : result;
        return (int) r == r ? (int) r : 0;
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
            if (num <= 0) {
                break;
            }
        }
        return list;
    }

}
