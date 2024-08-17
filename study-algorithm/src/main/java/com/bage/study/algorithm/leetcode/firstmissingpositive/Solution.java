package com.bage.study.algorithm.leetcode.firstmissingpositive;

/**
 * https://leetcode.cn/problems/first-missing-positive/description/
 *
 * 提示：
 * 遍历一次数组把大于等于1的和小于数组大小的值放到原数组对应位置，
 * 然后再遍历一次数组查当前下标是否和值对应，
 * 如果不对应那这个下标就是答案，
 * 否则遍历完都没出现那么答案就是数组长度加1。
 *
 */
class Solution {

    public int firstMissingPositive(int[] nums) {
        int min = 0;
        int expectMin = 0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if(num > 0 && min == 0){
                // min 还没有赋值过，存在正数，先记录着
                min = num;
            }
            if(num < min){
                min = num;
            }
        }
        if(min == 0){

        }
        return min + 1;
    }


    public static void main(String[] args) {
        System.out.println(new Solution().firstMissingPositive(new int[]{1,3,5,6}));
        System.out.println(new Solution().firstMissingPositive(new int[]{1,2,0}));
        System.out.println(new Solution().firstMissingPositive(new int[]{3,4,-1,1}));
        System.out.println(new Solution().firstMissingPositive(new int[]{7,8,9,11,12}));
    }
}