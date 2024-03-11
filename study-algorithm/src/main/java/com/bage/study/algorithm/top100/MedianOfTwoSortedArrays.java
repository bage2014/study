package com.bage.study.algorithm.top100;

public class MedianOfTwoSortedArrays {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length == 0) {
            return findMedianSortedArrays(nums2);
        }
        if (nums2.length == 0) {
            return findMedianSortedArrays(nums1);
        }
        int total = nums1.length + nums2.length;
        int middleIndex = (total + 1) / 2; // +1 保证基数下能多一次执行  == 2

        int nums1Index = 0;
        int nums2Index = 0;

        int count = 0;
        int currentValue = -1;
        int nextValue = -1;
        while (count < middleIndex) { // 找到中间位置
            if (nums1[nums1Index] <= nums2[nums2Index]) {
                currentValue = nums1[nums1Index];
                if (nums1Index < nums1.length - 1) {
                    // nums1 比较小 并且 index1 还能移动
                    nums1Index++;
                    nextValue = nums1[nums1Index] <= nums2[nums2Index] ? nums1[nums1Index]:nums2[nums2Index];

                } else {
                    // nums1 比较小 但是 index1 还能移动，只能移动 index2
                    nextValue = nums2[nums2Index];
                    nums2Index++;
                }
            } else {
                currentValue = nums2[nums2Index];
                if (nums2Index < nums2.length - 1) {
                    nums2Index++;
                    nextValue = nums2[nums2Index] <= nums1[nums1Index] ? nums2[nums2Index]:nums1[nums1Index];
                } else {
                    nextValue = nums1[nums1Index];
                    nums1Index++;
                }
            }
            count++;
        }
        if (total % 2 == 1) {
            return currentValue;
        }
        // 偶数
        return (currentValue + nextValue) / 2.0;
    }

    private double findMedianSortedArrays(int[] nums) {
        if (nums.length % 2 == 1) {
            return nums[(nums.length) / 2];
        }
        //  nums.length % 2 == 0
        return (nums[(nums.length - 1) / 2] + nums[nums.length / 2]) / 2.0;
    }
}
