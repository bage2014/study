package com.bage.study.algorithm.leetcode.permutationsequence;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/permutation-sequence/
 * 给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。
 * <p>
 * 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下：
 * <p>
 * "123"
 * "132"
 * "213"
 * "231"
 * "312"
 * "321"
 * 给定 n 和 k，返回第 k 个排列。
 */
class Solution {

    public String getPermutation(int n, int k) {
        int[] arr = new int[n];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i + 1;
        }

        List<String> list = new ArrayList<>();
        return getPermutation(n,k,arr,list);
    }

    private String getPermutation(int n, int k, int[] arr, List<String> list) {
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new Solution().getPermutation(2, 4));
    }
}