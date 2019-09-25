package com.bage.study.algorithm.sort;

import com.bage.study.algorithm.DataUtils;

/**
 * 对于大规模乱序数组插入排序很慢，因为它只会交换相邻的元素，因此元素只能一点一点地从数组的一端移动到另一端。<br>
 * 例如，如果主键最小的元素正好在数组的尽头，要将它挪到正确的位置就需要 N-1 次移动。<br>
 * 希尔排序为了加快速度简单地改进了插入排序，交换不相邻的元素以对数组的局部进行排序，<br>
 * 并最终用插入排序将局部有序的数组排序。<br>
 * 希尔排序的思想是使数组中任意间隔为 h 的元素都是有序的。<br>
 * 这样的数组被称为 h 有序数组。换句话说，一个 h 有序数组就是 h 个互相独立的有序数组编织在一起组成的一个数组<br>
 * 在进行排序时，如果 h 很大，我们就能将元素移动到很远的地方，为实现更小的 h 有序创造方便。<br>
 * 用这种方式，对于任意以 1 结尾的 h 序列，我们都能够将数组排序，这就是希尔排序。<br>
 */
public class ShellSort {

    public static void main(String[] args) {
        int[] a = DataUtils.init(10);
        DataUtils.print(a);
        new ShellSort().shellSort(a);
        DataUtils.print(a);
    }

    public void shellSort(int[] a) {
        int currentValue = 0;
        int targetIndex = 0;
        int space = a.length / 2;
        while (space > 1) {
            for (int i = 0; i < a.length; i = i + space) { // 从第一个开始
                currentValue = a[i]; // 当前等待插入元素
                targetIndex = findIndex(a, currentValue, i, space); // 查找下标
                moveAndInsert(a, currentValue, i, targetIndex, space); // 移动插入
            }
            space = space / 2;
        }

    }

    /**
     * 在中间进行插入，先后移在赋值
     * @param a
     * @param currentValue
     * @param currentIndex
     * @param targetIndex
     * @param space
     */
    private void moveAndInsert(int[] a, int currentValue, int currentIndex, int targetIndex, int space) {
        for (int i = currentIndex; i > targetIndex; i = i - space) {
            a[i] = a[i - space];
        }
        a[targetIndex] = currentValue;
    }

    /**
     * 找到第一个比当前值小的值得下标
     * @param a
     * @param currentValue
     * @param currentIndex
     * @param space
     * @return
     */
    private int findIndex(int[] a, int currentValue, int currentIndex, int space) {
        for (int i = currentIndex - space; i >= 0; i = i - space) { // 找到第一个比当前值小的值的位置
            if(a[i] < currentValue ){ // 说明插入此位置
                return i + space;
            }
        }
        return currentIndex; // 插入到第一个位置
    }

}
