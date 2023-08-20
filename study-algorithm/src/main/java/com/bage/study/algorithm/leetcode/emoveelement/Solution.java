package com.bage.study.algorithm.leetcode.emoveelement;

/**
 * https://leetcode.cn/problems/remove-element/
 */
class Solution {
    public int removeElement(int[] nums, int val) {
        int newLength = nums.length;
        if (newLength == 0) {
            return newLength;
        }
        int last = newLength - 1;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == val) {
                // remove
                while (last > i) {
                    if (nums[last] != val) {
                        int temp = nums[last];
                        nums[last] = nums[i];
                        nums[i] = temp;
                        break;
                    }
                    last--;
                }
                newLength--;
            }
            if(i >= newLength){
                break;
            }
        }
        return newLength;
    }

    public static void main(String[] args) {
        int[] nums = new int[0];
        int i = 0;
//        nums = new int[]{3, 5, 10,77, 24, 25, 77, 96};
//        i = new Solution().removeElement(nums, 77);
//        print(nums,i);
//
//        nums = new int[]{3, 2,2, 3};
//        i = new Solution().removeElement(nums, 3);
//        print(nums,i);
//
//        nums = new int[]{0,1,2,2,3,0,4,2};
//        i = new Solution().removeElement(nums, 2);
//        print(nums,i);
//
//        nums = new int[]{2, 2};
//        i = new Solution().removeElement(nums, 2);
//        print(nums, i);
//
//        nums = new int[]{2, 2};
//        i = new Solution().removeElement(nums, 3);
//        print(nums, i);

        nums = new int[]{2, 3};
        i = new Solution().removeElement(nums, 2);
        print(nums, i);

    }

    private static void print(int[] nums, int i) {
        for (int j = 0; j < i; j++) {
            System.out.print(nums[j] + ",");
        }
        System.out.println();
    }
}