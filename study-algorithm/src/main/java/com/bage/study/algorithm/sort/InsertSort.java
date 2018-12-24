package com.bage.study.algorithm.sort;

/**
 * 插入排序：https://www.lintcode.com/problem/sort-integers/description
 *
 */
public class InsertSort {

    public static void main(String[] args) {

        int a[] = {2,1,3,54,6,7,9,2,4};

        InsertSort  InsertSort = new InsertSort();
        InsertSort.insertSort(a);
        InsertSort.myPrint(a);
    }

    private void myPrint(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + "\t");
        }
        System.out.println();
    }

    /**
     * @param A: an integer array
     * @return: nothing
     */
    public void sortIntegers(int[] A) {
        // write your code here
        insertSort(A);
    }

    private void insertSort(int[] a) {

        int currentValue = 0;
        int targetIndex = 0;
        for (int i = 0; i < a.length; i++) { // 从第一个开始
            currentValue = a[i]; // 当前等待插入元素
            targetIndex = findIndex(a,currentValue,i); // 查找下标
            moveAndInsert(a,currentValue,i,targetIndex); // 移动插入
        }

    }

    /**
     * 在中间进行插入，先后移在赋值
     * @param a
     * @param currentValue
     * @param currentIndex
     * @param targetIndex
     */
    private void moveAndInsert(int[] a, int currentValue, int currentIndex, int targetIndex) {
        for (int i = currentIndex; i > targetIndex; i --) {
            a[i] = a[i - 1];
        }
        a[targetIndex] = currentValue;
    }

    /**
     * 找到第一个比当前值小的值得下标
     * @param a
     * @param currentValue
     * @param currentIndex
     * @return
     */
    private int findIndex(int[] a, int currentValue,int currentIndex) {
        for (int i = currentIndex - 1; i >= 0; i --) { // 找到第一个比当前值小的值的位置
            if(a[i] < currentValue ){ // 说明插入此位置
                return i + 1;
            }
        }
        return 0; // 插入到第一个位置
    }

}
