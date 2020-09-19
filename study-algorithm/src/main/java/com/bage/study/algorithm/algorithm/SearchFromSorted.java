package com.bage.study.algorithm.algorithm;

public class SearchFromSorted {

    public static void main(String[] args) {
        System.out.println(new SearchFromSorted().search(5,4,new int[]{
                1,2,4,4,5
        }));
    }

    /**
     * 二分查找
     *
     * @param n int整型 数组长度
     * @param v int整型 查找值
     * @param a int整型一维数组 有序数组
     * @return int整型
     */
    public int search(int n, int v, int[] a) {

        int start = 0;
        int end = n - 1;

        while (start < end) {
            int middleIndex = start + (end - start) / 2;
            int middleValue = a[middleIndex];
            if (middleValue == v) {
                end = middleIndex;
            } else if (middleValue > v) {
                // before
                end = middleIndex - 1;
            } else {
                // middleValue < v
                start = middleIndex + 1;
            }
        }
        return start <= 0 ? n : start + 1;
        // write code here
    }

}
