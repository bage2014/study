package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * https://leetcode.cn/problems/combination-sum/
 */
public class CombinationSum {

    public static void main(String[] args) {
        new CombinationSum().combinationSum(new int[]{2,3,6,7},7);
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        combinationSumR(candidates, target, result, new ArrayList<>());
        return result;
    }

    private void combinationSumR(int[] candidates, int left, List<List<Integer>> result, List<Integer> temp) {
        for (int i = 0; i < candidates.length; i++) {
            if (candidates[i] == left) {
                // match
                int currentIndex = temp.size();
                temp.add(candidates[i]);
                DataUtils.print(temp);
                // result.add(temp);
                temp.remove(currentIndex); // 清除
                return;
            } else if(candidates[i] < left){
                // continue && next
                int currentIndex = temp.size();
                temp.add(candidates[i]);
                combinationSumR(candidates, left - candidates[i], result, temp);
                temp.remove(currentIndex);
            } else {
                // continue
            }
        }
    }

}
