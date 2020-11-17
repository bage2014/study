package com.bage.study.algorithm.array;

public class Solution {


    /**
     * 寻找数组最长递增子序列，例如[1, 2, 3, 5, 4, 7]，最长递增子序列喂1，2，3 返回 3
     * @param nums
     * @return
     */
    public int findLengthOfLCIS(int[] nums) {

        //算是动态规划问题，当前数组的最长子序列为子数组的最长子序列或者是包含最后一个值的最长子序列
        if(nums.length>0){
            int max = 1;        //子数组的最长子序列
            int maxCurrent = 1; //包含当前值的最长子序列

            for(int i=1; i<nums.length; ++i){
                maxCurrent = nums[i]>nums[i-1]?maxCurrent+1:1;  //包含当前值的最长子序列为子数组包含当前值的最长子序列加一或者是一
                max = Math.max(maxCurrent, max);               
            }

            return max;

        }

        return 0;
    }
}
