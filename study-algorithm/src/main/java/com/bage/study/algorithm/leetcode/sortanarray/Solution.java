package com.bage.study.algorithm.leetcode.sortanarray;

import java.util.Arrays;

/**
 * https://leetcode.cn/problems/sort-an-array/
 * 快速排序
 */
class Solution {
    public int[] sortArray(int[] nums) {
        quickSort(nums, 0, nums.length - 1);
        return nums;
    }

    private void quickSort(int[] nums, int start, int end) {
        if (start == end) {
            return;
        }

        int middleValue = nums[start]; // 第一个
        int leftIndex = start + 1;
        int rightIndex = end;

        while (leftIndex < rightIndex) {
            while (nums[leftIndex] <= middleValue) {
                leftIndex++;
            }
            while (nums[rightIndex] >= middleValue) {
                rightIndex--;
            }

            if (leftIndex >= rightIndex) {
                break;
            }
            // 交换
            int temp = nums[leftIndex];
            nums[leftIndex] = nums[rightIndex];
            nums[rightIndex] = temp;
        }

        quickSort(nums, start, leftIndex - 1);
        quickSort(nums, leftIndex + 1, end);
    }

    public static void main(String[] args) {
        int[] nums = new int[]{5, 2, 3, 1};
        System.out.println(Arrays.toString(new Solution().sortArray(nums)));
    }
}