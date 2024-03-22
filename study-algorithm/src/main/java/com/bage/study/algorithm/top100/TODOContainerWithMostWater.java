package com.bage.study.algorithm.top100;

/**
 * https://leetcode.cn/problems/container-with-most-water/description/
 *
 * // todo bage for + 双指针 超出时间限制
 *
 */
public class TODOContainerWithMostWater {

    public static void main(String[] args) {
        int[] height = {1,8,6,2,5,4,8,3,7};
        System.out.println(new TODOContainerWithMostWater().maxArea(height));
    }

    public int maxArea(int[] height) {
        return maxArea2(height);
    }

    public int maxArea1(int[] height) {
        int max = 0;
        if(height.length == 0){
            return max;
        }
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                int w = j - i > 0 ? (j-i) : (i-j);
                int h = height[i] < height[j] ? height[i] : height[j];
                int temp = w * h;
                if(temp > max){
                    max = temp;
                }
            }
        }
        return max;
    }


    public int maxArea2(int[] height) {
        int max = 0;
        if(height.length == 0){
            return max;
        }
        for (int i = 0; i < height.length; i++) {
            for (int j = i + 1; j < height.length; j++) {
                int w = j - i > 0 ? (j-i) : (i-j);
                int h = height[i] < height[j] ? height[i] : height[j];
                int temp = w * h;
                if(temp > max){
                    max = temp;
                }
            }
        }
        return max;
    }


}
