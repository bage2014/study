package com.bage.study.algorithm.top100;

/**
 * https://leetcode.cn/problems/container-with-most-water/description/
 *
 * // for + 双指针
 *
 */
public class ContainerWithMostWater {

    public static void main(String[] args) {
        int[] height = {1,8,6,2,5,4,8,3,7};
        System.out.println(new ContainerWithMostWater().maxArea(height));
    }

    public int maxArea(int[] height) {
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
