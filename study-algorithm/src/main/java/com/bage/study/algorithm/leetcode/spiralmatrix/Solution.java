package com.bage.study.algorithm.leetcode.spiralmatrix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * https://leetcode.cn/problems/spiral-matrix/
 * <p>
 * 给你一个 m 行 n 列的矩阵 matrix ，请按照 顺时针螺旋顺序 ，返回矩阵中的所有元素。
 *
 * todo bage 未完成、待续
 */
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> list = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                count++;
            }
        }
        int i = 0;
        int j = 0;
        while (count >= 0) {
            count--;
            list.add(matrix[i][j]);

            if (i == 0 && j < matrix[i].length - 1) {
                // 没有 到最后一个
                j++;
            } else if (j == matrix[j].length - 1 && i < matrix[j].length - 1) {
                // 没有 到最后一个
                i++;
            } else if (i == matrix[j].length - 1 && j == matrix[i].length - 1) {
                // 没有 到最后一个
                j--;
            } else if (j == 0 && j == matrix[i].length - 1) {
                // 没有 到最后一个
                i--;
            }
        }
        return list;
    }

    public static void main(String[] args) {
        int[][] matrix = new int[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };
        System.out.println(Arrays.toString(new Solution().spiralOrder(matrix).toArray()));
    }
}