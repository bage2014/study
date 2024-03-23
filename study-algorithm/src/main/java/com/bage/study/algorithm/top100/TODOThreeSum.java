package com.bage.study.algorithm.top100;

import com.bage.study.algorithm.DataUtils;

import java.util.*;

/**
 * https://leetcode.cn/problems/3sum/description/
 *
 * // todo bage 超出时间限制
 */
public class TODOThreeSum {

    public static void main(String[] args) {

        System.out.println(DataUtils.toStringFromInt(new TODOThreeSum().threeSum(new int[]{-1,0,1,2,-1,-4})));
        System.out.println(DataUtils.toStringFromInt(new TODOThreeSum().threeSum(new int[]{-1,0,1,1,2,1,-1,-4})));

//        System.out.println(new TODOThreeSum().isMatch(Arrays.asList(1,2,3),Arrays.asList(2,2,3)));
//        System.out.println(new TODOThreeSum().isMatch(Arrays.asList(1,2,3),Arrays.asList(1,2,3)));
//        System.out.println(new TODOThreeSum().isMatch(Arrays.asList(1,2,3),Arrays.asList(3,2,1)));


    }

    public List<List<Integer>> threeSum(int[] nums) {
        int sum = 0;
        List<List<Integer>> listList = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            for (int j = i; j < nums.length; j++) {
                if(i == j){
                    continue; // 跳过
                }
                for (int k = j; k < nums.length; k++) {
                    if(j == k || i == k){
                        continue; // 跳过
                    }
                    int current = nums[i] + nums[j] + nums[k];
                    if(current == sum){
                        addToList(listList,nums[i], nums[j], nums[k]);
                    }
                }
            }
        }
        return listList;
    }

    private void addToList(List<List<Integer>> listList, int i, int j, int k) {
        List<Integer> list = new ArrayList<>();
        list.add(i);
        list.add(j);
        list.add(k);
        boolean canAdd = true;
//        for (int i1 = 0; i1 < listList.size(); i1++) {
//            List<Integer> nowList = listList.get(i1);
//            if(isMatch(nowList,list)){
//                canAdd = false;
//                break;
//            }
//        }
        if(canAdd){
            listList.add(list);
        }
    }

    private boolean isMatch(List<Integer> nowList, List<Integer> list) {
        boolean[] isUsedInNow = new boolean[nowList.size()];
        Arrays.fill(isUsedInNow,false); //  初始化 false

        for (int i = 0; i < list.size(); i++) {
            boolean isFound = false;
            for (int j = 0; j < nowList.size(); j++) {
                if(list.get(i) == nowList.get(j) && !isUsedInNow[j]){
                    isUsedInNow[j] = true;
                    isFound = true;
                    break;
                }
            }
            if(!isFound){ // 只要有一个元素。在nowList 里面没有满足，就返回false
                return false;
            }
        }
        return true;
    }
}
