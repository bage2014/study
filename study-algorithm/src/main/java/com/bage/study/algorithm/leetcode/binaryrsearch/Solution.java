package com.bage.study.algorithm.leetcode.binaryrsearch;

/**
 * https://leetcode.cn/problems/binary-search/
 */
class Solution {
    public static void main(String[] args) {
        int[] nums = {3, 5, 10, 24, 25, 77, 96};
        System.out.println(new Solution().search(nums, 77));
        System.out.println(new Solution().search(nums, 7));
    }

    public int search(int[] nums, int target) {

        int left = 0;
        int right = nums.length - 1;
        while (left <= right) { // 等于也可以进来查找
            int middle = (left + right) / 2;
            int middleValue = nums[middle];

            if (target == middleValue) {
                return middle;
            } else if (target > middleValue) {
                // 目标值 大于 中间值
                // 比如 目标值：77， 数组： 3 5 10 24 25 77 96
                // 继续右边查找
                left = middle + 1;
            } else {
                // target < middleValue
                // 继续左边查找
                right = middle - 1;
            }
        }

        return -1;
    }
}
