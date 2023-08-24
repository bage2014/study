package com.bage.study.algorithm.leetcode.countserversthatcommunicate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.cn/problems/count-servers-that-communicate/
 */
class Solution {
    public int countServers(int[][] grid) {
        Map<Integer, Integer> mapI = new HashMap<>();
        Map<Integer, Integer> mapJ = new HashMap<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] > 0) {
                    // 存在
                    mapI.put(i, mapI.getOrDefault(i, 0) + 1);
                    mapJ.put(j, mapJ.getOrDefault(j, 0) + 1);
                }
            }
        }
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] > 0
                        && (mapI.getOrDefault(i, 0) > 1
                        || mapJ.getOrDefault(j, 0) > 1)) {
                    // 存在
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int[][] arr = {{1, 0}, {1, 1}};
        System.out.println(new Solution().countServers(arr));

        arr = new int[][]{{1, 0}, {0, 1}};
        System.out.println(new Solution().countServers(arr));

    }
}