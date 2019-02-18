package com.bage.study.algorithm.sort;

import com.bage.study.algorithm.PrintUtils;

/**
 * 一种最简单的排序算法是这样的：<br/>
 * 首先，找到数组中最小的那个元素，其次，将它和数组的第一个元素交换位置<br/>
 * （如果第一个元素就是最小元素那么它就和自己交换）。<br/>
 * 再次，在剩下的元素中 * 找到最小的元素，将它与数组的第二个元素交换位置。<br/>
 * 如此往复，直到将整个数组排序。这种方法叫做选择排序，因为它在不断地选择剩余元素之中的最小者<br/>
 */
public class SelectSort {

    public static void main(String[] args) {

        int a[] = {2,3,5,7,1,9,6,4,8};
        new SelectSort().selectSort(a);
        PrintUtils.print(a);
    }

    /**
     *  选择排序（每次选择最值）
     * @param a
     */
    public void selectSort(int[] a) {
        int minIndex = 0;
        int temp = 0;
        for (int i = 0; i < a.length - 1; i++) { // 循环次数为 ( a.length - 1 )
            minIndex = i;
            for (int j = i + 1; j < a.length; j++) {
                if(a[j] < a[minIndex] ){ // 如果当前值小于最小值
                    minIndex = j; // 记录最小值的下标
                }
            }
            // 将当前i跟最小值进行交换
            temp = a[i];
            a[i] = a[minIndex];
            a[minIndex] = temp;
        }

    }

}
