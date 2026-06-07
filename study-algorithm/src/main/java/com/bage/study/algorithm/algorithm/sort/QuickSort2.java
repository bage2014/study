package com.bage.study.algorithm.algorithm.sort;

public class QuickSort2 {

    public static void main(String[] args) {
        int[] arr = {3, 2, 1, 5, 4};
        quickSort(arr, 0, arr.length - 1);
        for (int i : arr) {
            System.out.println(i);
        }
    }

    private static void quickSort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }

        int p = start + (end - start) / 2;

        int i = start;
        int j = end;
        while (i <= j) {
            while (i <= j && arr[i] < arr[p]) {
                i++;
            }
            while (i <= j && arr[j] > arr[p]) {
                j--;
            }

            if (i <= j) {
                swap(arr, i, j);
                i++;
                j--;
            }
        }
        quickSort(arr, start, j);
        quickSort(arr, i, end);
    }

    private static int swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
        return 0;

    }
}
