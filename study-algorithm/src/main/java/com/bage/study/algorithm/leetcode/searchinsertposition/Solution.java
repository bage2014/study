package com.bage.study.algorithm.leetcode.searchinsertposition;

/**
 * https://leetcode.cn/problems/search-insert-position/
 *
 */
class Solution {

    public static void main(String[] args) {
//        int[] nums = new int[]{1,2,3,4,5,6,7,8};
        System.out.println(new Solution().searchInsert(new int[]{1,3,5,6},2));
        System.out.println(new Solution().searchInsert(new int[]{1,3,5,6},4));
        System.out.println(new Solution().searchInsert(new int[]{1,3},3));
        System.out.println(new Solution().searchInsert(new int[]{1,3},2));
        System.out.println(new Solution().searchInsert(new int[]{1,3},5));
        System.out.println(new Solution().searchInsert(new int[]{2,3},1));
        System.out.println(new Solution().searchInsert(new int[]{1,3},1));
    }
    public int searchInsert(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        // 两边都可以比较
        if(nums.length == 0 || nums[0] >= target ){
            return 0;
        }
        if(nums[nums.length-1] < target ){
            return nums.length;
        }

        while(left <= right){ // 等于直接退出
            int middle = (left + right) / 2;
            int middleValue = nums[middle];

            if(target == middleValue){
//                return middle;
                // 继续左边查找
                right = middle - 1;
            } else if(target > middleValue){
                // 目标值 大于 中间值
                // 比如 目标值：77， 大于24 ，， 数组： 3 5 10 24 25 77 96
                // 继续右边查找
                left = middle + 1;
            } else {
                // target < middleValue
                // 继续左边查找
                right = middle - 1;
            }
        }
        return left;
    }


}