package com.bage.study.algorithm.top100;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/two-sum/description/
 */
public class TwoSum {
    public static void main(String[] args) {
        int[] ints = new Solution().twoSum(new int[]{2,7,11,15}, 9);
        System.out.println(Arrays.toString(ints));
        ints = new Solution().twoSum(new int[]{3,2,4}, 6);
        System.out.println(Arrays.toString(ints));
        ints = new Solution().twoSum(new int[]{3,3}, 6);
        System.out.println(Arrays.toString(ints));
    }


    static class Solution {
        public int[] twoSum(int[] nums, int target) {
            Map<Integer,Integer> map = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                int num = nums[i];
                map.put(num,i);
            }

            for (int i = 0; i < nums.length; i++) {
                int leftNum = target - nums[i];
                int index = map.getOrDefault(leftNum,-1);
                if(index >= 0 && index != i){
                    return new int[]{i,index};
                };
            }
            return new int[0];
        }
    }

}


