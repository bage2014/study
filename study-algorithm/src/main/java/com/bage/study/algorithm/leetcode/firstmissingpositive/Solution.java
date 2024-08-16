package com.bage.study.algorithm.leetcode.firstmissingpositive;

class Solution {
    public int firstMissingPositive(int[] nums) {
        int min = 0;
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if(num > 0 && num > min){
                min = num;
            }
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