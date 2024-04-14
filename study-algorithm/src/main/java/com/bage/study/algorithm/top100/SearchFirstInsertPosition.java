package com.bage.study.algorithm.top100;

/**
 * https://leetcode.cn/problems/search-insert-position/description/
 * 改进版本 寻找第一个插入下标
 */
public class SearchFirstInsertPosition {

    public static void main(String[] args) {
        int[] nums = new int[]{1,2,3,4,5,6,7,8,8,8,8};
        System.out.println(new SearchFirstInsertPosition().searchInsert(nums,5));
        System.out.println(new SearchFirstInsertPosition().searchInsert(nums,8));
        System.out.println(new SearchFirstInsertPosition().searchInsert(nums,-1));
        System.out.println(new SearchFirstInsertPosition().searchInsert(nums,100));
        System.out.println(new SearchFirstInsertPosition().searchInsert(new int[]{},50));
        System.out.println(new SearchFirstInsertPosition().searchInsert(new int[]{50},50));
        System.out.println(new SearchFirstInsertPosition().searchInsert(new int[]{1, 3, 5, 6}, 2));
        System.out.println(new SearchFirstInsertPosition().searchInsert(new int[]{1, 3, 5, 6}, 4));
    }

    public int searchInsert(int[] nums, int target) {
        if(nums.length == 0){
            return -1;
        }
        int left = 0;
        int right = nums.length - 1;
        while (left < right) { // 等于直接退出
            int middle = (left + right) / 2;
            int middleValue = nums[middle];
            if (target == middleValue) {
                // 继续左边查找
                right = middle;
            } else if (target > middleValue) {
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
        if (nums[left] == target) {
            return left;
        }
        return -1;
    }
}
