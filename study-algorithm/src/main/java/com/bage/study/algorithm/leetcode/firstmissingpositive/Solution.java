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

    /**
     * 答案肯定在 0-N 之间
     * @param nums
     * @return
     */
    public int firstMissingPositive(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            // 不在0-N 之间的，或者 小于0 的，直接更新为 -1
            if(nums[i] > nums.length || nums[i] <= 0){
                nums[i] = -1;
            }
        }

        for (int i = 0; i < nums.length; i++) {
            if(nums[i] > nums.length || nums[i] <= 0){
                nums[i] = -1;
            }
        }

        return 1;
    }


    public static void main(String[] args) {
        System.out.println(new Solution().firstMissingPositive(new int[]{1,3,5,6}));
        System.out.println(new Solution().firstMissingPositive(new int[]{1,2,0}));
        System.out.println(new Solution().firstMissingPositive(new int[]{3,4,-1,1}));
        System.out.println(new Solution().firstMissingPositive(new int[]{7,8,9,11,12}));
    }
}