# 算法与数据结构

## 目录

- [算法与数据结构](#算法与数据结构)
  - [目录](#目录)
  - [1. 基础概念](#1-基础概念)
  - [2. 排序算法](#2-排序算法)
    - [2.1 常见排序算法](#21-常见排序算法)
    - [2.2 排序算法比较](#22-排序算法比较)
  - [3. 搜索算法](#3-搜索算法)
  - [4. 递归与分治](#4-递归与分治)
  - [5. 动态规划](#5-动态规划)
  - [6. 暴力算法](#6-暴力算法)
  - [7. 贪心算法](#7-贪心算法)
  - [8. 回溯算法](#8-回溯算法)
  - [9. 分支限界算法](#9-分支限界算法)
  - [10. 数据结构](#10-数据结构)
    - [6.1 线性结构](#61-线性结构)
    - [6.2 非线性结构](#62-非线性结构)
    - [6.3 跳跃表](#63-跳跃表)
    - [6.4 红黑树 vs 跳跃表](#64-红黑树-vs-跳跃表)
  - [7. 一致性算法](#7-一致性算法)
    - [7.1 为什么需要一致性](#71-为什么需要一致性)
    - [7.2 一致性分类](#72-一致性分类)
    - [7.3 主流一致性算法](#73-主流一致性算法)
  - [8. 面试题解析](#8-面试题解析)
  - [9. 参考链接](#9-参考链接)

## 1. 基础概念

算法是解决问题的步骤集合，数据结构是组织和存储数据的方式。好的算法和数据结构可以显著提高程序的效率和可维护性。

- **时间复杂度**：衡量算法执行时间随输入规模增长的变化趋势
- **空间复杂度**：衡量算法所需存储空间随输入规模增长的变化趋势
- **稳定性**：排序算法中，相同值的元素在排序后相对位置是否保持不变

## 2. 排序算法

### 2.1 基本概念

排序算法是一种将一组数据按照特定顺序（如升序或降序）排列的算法。排序是计算机科学中最基本、最常用的操作之一，在数据处理、检索、统计等领域有着广泛的应用。

### 2.2 算法思路

排序算法的基本思路是通过比较和交换元素，将无序的数据转换为有序的数据。根据不同的策略，排序算法可以分为：

1. **比较排序**：通过比较元素大小来决定元素的顺序，如冒泡排序、选择排序、插入排序、归并排序、快速排序、堆排序
2. **非比较排序**：不通过比较元素大小，而是利用元素的其他特性来排序，如计数排序、桶排序、基数排序

### 2.3 优缺点

**比较排序的优缺点**：
- **优点**：适用于任何数据类型，不受元素范围限制
- **缺点**：时间复杂度下限为O(nlogn)，无法突破

**非比较排序的优缺点**：
- **优点**：时间复杂度可以达到线性O(n)，效率更高
- **缺点**：只适用于特定类型的数据，如整数、字符串等，且受元素范围限制

### 2.4 可优化项

1. **选择合适的排序算法**：根据数据规模、数据类型、内存限制等选择合适的排序算法
2. **优化比较次数**：减少元素之间的比较次数
3. **优化交换次数**：减少元素之间的交换次数
4. **利用缓存**：优化数据访问模式，提高缓存命中率
5. **并行化**：利用多核CPU进行并行排序
6. **混合排序**：结合多种排序算法的优点，如TimSort（结合归并排序和插入排序）

### 2.5 常见排序算法

#### 2.5.1 冒泡排序

**基本思想**：通过相邻元素的比较和交换，使较大的元素逐渐向后移动

**时间复杂度**：O(n²)
**空间复杂度**：O(1)
**稳定性**：稳定

**样例代码**：

```java
public class BubbleSort {
    public static int[] bubbleSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            // 标记是否发生交换，若未发生则数组已有序
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    // 交换元素
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
        return arr;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = bubbleSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

#### 2.5.2 选择排序

**基本思想**：每次选择未排序部分的最小元素，放到已排序部分的末尾

**时间复杂度**：O(n²)
**空间复杂度**：O(1)
**稳定性**：不稳定

**样例代码**：

```java
public class SelectionSort {
    public static int[] selectionSort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            // 找到未排序部分的最小元素
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            // 交换元素
            int temp = arr[i];
            arr[i] = arr[minIdx];
            arr[minIdx] = temp;
        }
        return arr;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = selectionSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

#### 2.5.3 插入排序

**基本思想**：将元素逐个插入到已排序的部分中

**时间复杂度**：O(n²)
**空间复杂度**：O(1)
**稳定性**：稳定

**样例代码**：

```java
public class InsertionSort {
    public static int[] insertionSort(int[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            // 将大于key的元素向右移动
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        return arr;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = insertionSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

#### 2.5.4 归并排序

**基本思想**：分治策略，将数组分成两半，分别排序后合并

**时间复杂度**：O(nlogn)
**空间复杂度**：O(n)
**稳定性**：稳定

**样例代码**：

```java
public class MergeSort {
    public static int[] mergeSort(int[] arr) {
        if (arr.length <= 1) {
            return arr;
        }
        
        // 分解
        int mid = arr.length / 2;
        int[] left = new int[mid];
        int[] right = new int[arr.length - mid];
        
        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, arr.length - mid);
        
        left = mergeSort(left);
        right = mergeSort(right);
        
        // 合并
        return merge(left, right);
    }
    
    public static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        
        while (i < left.length && j < right.length) {
            if (left[i] < right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        
        while (i < left.length) {
            result[k++] = left[i++];
        }
        
        while (j < right.length) {
            result[k++] = right[j++];
        }
        
        return result;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = mergeSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

#### 2.5.5 快速排序

**基本思想**：选择一个基准元素，将数组分为两部分，小于基准的在左，大于的在右，递归处理

**时间复杂度**：O(nlogn)，最坏情况O(n²)
**空间复杂度**：O(logn)
**稳定性**：不稳定

**样例代码**：

```java
public class QuickSort {
    public static int[] quickSort(int[] arr) {
        if (arr.length <= 1) {
            return arr;
        }
        
        // 选择基准元素
        int pivot = arr[arr.length / 2];
        
        // 统计各部分元素数量
        int leftCount = 0, middleCount = 0, rightCount = 0;
        for (int num : arr) {
            if (num < pivot) leftCount++;
            else if (num == pivot) middleCount++;
            else rightCount++;
        }
        
        // 填充各部分数组
        int[] left = new int[leftCount];
        int[] middle = new int[middleCount];
        int[] right = new int[rightCount];
        
        int l = 0, m = 0, r = 0;
        for (int num : arr) {
            if (num < pivot) left[l++] = num;
            else if (num == pivot) middle[m++] = num;
            else right[r++] = num;
        }
        
        // 递归排序
        left = quickSort(left);
        right = quickSort(right);
        
        // 合并结果
        int[] result = new int[arr.length];
        System.arraycopy(left, 0, result, 0, left.length);
        System.arraycopy(middle, 0, result, left.length, middle.length);
        System.arraycopy(right, 0, result, left.length + middle.length, right.length);
        
        return result;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = quickSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

#### 2.5.6 堆排序

**基本思想**：利用堆这种数据结构的特性进行排序

**时间复杂度**：O(nlogn)
**空间复杂度**：O(1)
**稳定性**：不稳定

**样例代码**：

```java
public class HeapSort {
    public static int[] heapSort(int[] arr) {
        int n = arr.length;
        
        // 构建最大堆
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        
        // 逐个提取元素
        for (int i = n - 1; i > 0; i--) {
            // 交换根节点与最后一个元素
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            
            // 对剩余堆进行调整
            heapify(arr, i, 0);
        }
        
        return arr;
    }
    
    public static void heapify(int[] arr, int n, int i) {
        int largest = i; // 初始化largest为根节点
        int left = 2 * i + 1; // 左子节点
        int right = 2 * i + 2; // 右子节点
        
        // 如果左子节点大于根节点
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }
        
        // 如果右子节点大于当前最大节点
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }
        
        // 如果最大节点不是根节点
        if (largest != i) {
            // 交换
            int temp = arr[i];
            arr[i] = arr[largest];
            arr[largest] = temp;
            
            // 递归调整受影响的子堆
            heapify(arr, n, largest);
        }
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = heapSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

#### 2.5.7 计数排序

**基本思想**：统计每个元素出现的次数，然后重建数组

**时间复杂度**：O(n+k)，k是元素的范围
**空间复杂度**：O(k)
**稳定性**：稳定

**样例代码**：

```java
public class CountingSort {
    public static int[] countingSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return arr;
        }
        
        // 找出最大值和最小值
        int maxVal = arr[0];
        int minVal = arr[0];
        for (int num : arr) {
            if (num > maxVal) {
                maxVal = num;
            }
            if (num < minVal) {
                minVal = num;
            }
        }
        
        int rangeOfElements = maxVal - minVal + 1;
        
        // 统计每个元素出现的次数
        int[] count = new int[rangeOfElements];
        for (int num : arr) {
            count[num - minVal]++;
        }
        
        // 重建数组
        int index = 0;
        for (int i = 0; i < rangeOfElements; i++) {
            while (count[i] > 0) {
                arr[index++] = i + minVal;
                count[i]--;
            }
        }
        
        return arr;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = countingSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

#### 2.5.8 桶排序

**基本思想**：将元素分配到有限数量的桶中，对每个桶单独排序

**时间复杂度**：O(n+k)
**空间复杂度**：O(n+k)
**稳定性**：稳定

**样例代码**：

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BucketSort {
    public static int[] bucketSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return arr;
        }
        
        // 找出最大值和最小值
        int maxVal = arr[0];
        int minVal = arr[0];
        for (int num : arr) {
            if (num > maxVal) {
                maxVal = num;
            }
            if (num < minVal) {
                minVal = num;
            }
        }
        
        // 创建桶
        int bucketCount = arr.length;
        int bucketSize = (maxVal - minVal) / bucketCount + 1;
        List<List<Integer>> buckets = new ArrayList<>(bucketCount);
        for (int i = 0; i < bucketCount; i++) {
            buckets.add(new ArrayList<>());
        }
        
        // 将元素分配到桶中
        for (int num : arr) {
            int index = (num - minVal) / bucketSize;
            buckets.get(index).add(num);
        }
        
        // 对每个桶排序并合并
        int index = 0;
        for (List<Integer> bucket : buckets) {
            // 对桶内元素排序
            Integer[] bucketArr = bucket.toArray(new Integer[0]);
            Arrays.sort(bucketArr);
            // 合并到原数组
            for (int num : bucketArr) {
                arr[index++] = num;
            }
        }
        
        return arr;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = bucketSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

#### 2.5.9 基数排序

**基本思想**：按照低位先排序，然后收集；再按照高位排序，然后收集

**时间复杂度**：O(n×k)
**空间复杂度**：O(n+k)
**稳定性**：稳定

**样例代码**：

```java
import java.util.ArrayList;
import java.util.List;

public class RadixSort {
    public static int[] radixSort(int[] arr) {
        if (arr == null || arr.length == 0) {
            return arr;
        }
        
        // 找到最大数
        int maxVal = arr[0];
        for (int num : arr) {
            if (num > maxVal) {
                maxVal = num;
            }
        }
        
        // 找到最大数的位数
        int maxDigits = 0;
        while (maxVal > 0) {
            maxDigits++;
            maxVal /= 10;
        }
        
        // 对每一位进行计数排序
        for (int i = 0; i < maxDigits; i++) {
            // 创建10个桶（0-9）
            List<List<Integer>> buckets = new ArrayList<>(10);
            for (int j = 0; j < 10; j++) {
                buckets.add(new ArrayList<>());
            }
            
            int divisor = (int) Math.pow(10, i);
            
            // 将元素分配到桶中
            for (int num : arr) {
                int digit = (num / divisor) % 10;
                buckets.get(digit).add(num);
            }
            
            // 合并桶
            int index = 0;
            for (List<Integer> bucket : buckets) {
                for (int num : bucket) {
                    arr[index++] = num;
                }
            }
        }
        
        return arr;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = radixSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

### 2.6 排序算法比较

| 排序算法 | 平均时间复杂度 | 最坏时间复杂度 | 最好时间复杂度 | 空间复杂度 | 稳定性 |
|---------|---------------|---------------|---------------|-----------|--------|
| 冒泡排序 | O(n²) | O(n²) | O(n) | O(1) | 稳定 |
| 选择排序 | O(n²) | O(n²) | O(n²) | O(1) | 不稳定 |
| 插入排序 | O(n²) | O(n²) | O(n) | O(1) | 稳定 |
| 归并排序 | O(nlogn) | O(nlogn) | O(nlogn) | O(n) | 稳定 |
| 快速排序 | O(nlogn) | O(n²) | O(nlogn) | O(logn) | 不稳定 |
| 堆排序 | O(nlogn) | O(nlogn) | O(nlogn) | O(1) | 不稳定 |
| 计数排序 | O(n+k) | O(n+k) | O(n+k) | O(k) | 稳定 |
| 桶排序 | O(n+k) | O(n²) | O(n) | O(n+k) | 稳定 |
| 基数排序 | O(n×k) | O(n×k) | O(n×k) | O(n+k) | 稳定 |

## 3. 搜索算法

### 3.1 基本概念

搜索算法是用于在数据结构中查找特定元素的算法，根据数据结构的不同和搜索策略的差异，可以分为多种类型。搜索算法的效率直接影响到程序的性能，特别是在处理大规模数据时。

### 3.2 算法思路

搜索算法的基本思路是根据特定的策略，在数据结构中查找目标元素。不同的搜索算法有不同的策略：

1. **线性搜索**：逐个检查每个元素，直到找到目标元素或遍历完整个数据结构
2. **二分搜索**：在有序数据结构中，通过不断将搜索范围减半来查找目标元素
3. **深度优先搜索**：从起始节点开始，沿着一条路径尽可能深入地探索，直到不能继续为止，然后回溯
4. **广度优先搜索**：从起始节点开始，按层次顺序探索所有节点
5. **哈希表搜索**：利用哈希函数将键映射到哈希表中的索引位置，实现常数时间的查找

### 3.3 优缺点

| 搜索算法 | 优点 | 缺点 |
|---------|------|------|
| 线性搜索 | 实现简单，适用于任何数据结构 | 时间复杂度高，O(n) |
| 二分搜索 | 时间复杂度低，O(logn) | 要求数据结构有序，只适用于支持随机访问的数据结构 |
| 深度优先搜索 | 空间复杂度低，O(V)，可用于寻找路径 | 可能陷入深度，找不到最短路径 |
| 广度优先搜索 | 可用于寻找最短路径 | 空间复杂度高，O(V) |
| 哈希表搜索 | 平均时间复杂度低，O(1) | 空间复杂度高，O(n)，哈希冲突可能影响性能 |

### 3.4 可优化项

1. **选择合适的搜索算法**：根据数据结构的类型、数据规模、查询频率等选择合适的搜索算法
2. **优化数据结构**：使用更高效的数据结构，如哈希表、二叉搜索树、跳表等
3. **预处理数据**：对数据进行排序、索引等预处理，提高搜索效率
4. **缓存**：缓存经常查询的结果，减少重复搜索
5. **并行化**：利用多核CPU进行并行搜索
6. **剪枝**：在搜索过程中，提前排除不可能包含目标元素的区域

### 3.5 常见搜索算法

#### 3.5.1 线性搜索

**基本思想**：从数据结构的一端开始，逐个检查每个元素，直到找到目标元素或遍历完整个数据结构

**时间复杂度**：O(n)，n为元素个数
**空间复杂度**：O(1)
**适用场景**：适用于小型数据集或无序数据集

**样例代码**：

```java
public class LinearSearch {
    public static int linearSearch(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int target = 25;
        int result = linearSearch(arr, target);
        System.out.println("元素 " + target + " 在数组中的索引位置: " + result);
        // 输出: 2
    }
}
```

#### 3.5.2 二分搜索

**基本思想**：在有序数组中，每次将搜索范围减半，通过比较中间元素与目标值的大小，确定目标值在左半部分还是右半部分

**时间复杂度**：O(logn)
**空间复杂度**：O(1)（迭代实现）或 O(logn)（递归实现）
**适用场景**：适用于有序数组的查找

**样例代码**：

```java
public class BinarySearch {
    public static int binarySearch(int[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2; // 避免整数溢出
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {11, 12, 22, 25, 34, 64, 90};
        int target = 25;
        int result = binarySearch(arr, target);
        System.out.println("元素 " + target + " 在数组中的索引位置: " + result);
        // 输出: 3
    }
}
```

#### 3.5.3 深度优先搜索 (DFS)

**基本思想**：从起始节点开始，沿着一条路径尽可能深入地探索，直到不能继续为止，然后回溯到上一个节点，继续探索其他路径

**时间复杂度**：O(V+E)，V是顶点数，E是边数
**空间复杂度**：O(V)，用于存储递归栈或访问标记
**适用场景**：适用于树和图的遍历，如寻找路径、检测环等

**样例代码**（递归实现）：

```java
import java.util.*;

public class DFS {
    public static void dfs(Map<Character, List<Character>> graph, char start, Set<Character> visited) {
        visited.add(start);
        System.out.print(start + " ");
        for (char neighbor : graph.get(start)) {
            if (!visited.contains(neighbor)) {
                dfs(graph, neighbor, visited);
            }
        }
    }

    // 测试
    public static void main(String[] args) {
        // 构建图
        Map<Character, List<Character>> graph = new HashMap<>();
        graph.put('A', Arrays.asList('B', 'C'));
        graph.put('B', Arrays.asList('D', 'E'));
        graph.put('C', Arrays.asList('F'));
        graph.put('D', new ArrayList<>());
        graph.put('E', Arrays.asList('F'));
        graph.put('F', new ArrayList<>());
        
        Set<Character> visited = new HashSet<>();
        System.out.print("DFS遍历: ");
        dfs(graph, 'A', visited);
        // 输出: A B D E F C
    }
}
```

#### 3.5.4 广度优先搜索 (BFS)

**基本思想**：从起始节点开始，按层次顺序（距离起始节点的远近）探索所有节点

**时间复杂度**：O(V+E)
**空间复杂度**：O(V)，用于存储队列
**适用场景**：适用于寻找最短路径、层次遍历等

**样例代码**：

```java
import java.util.*;

public class BFS {
    public static void bfs(Map<Character, List<Character>> graph, char start) {
        Set<Character> visited = new HashSet<>();
        Queue<Character> queue = new LinkedList<>();
        visited.add(start);
        queue.offer(start);
        
        while (!queue.isEmpty()) {
            char node = queue.poll();
            System.out.print(node + " ");
            for (char neighbor : graph.get(node)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
    }

    // 测试
    public static void main(String[] args) {
        // 构建图
        Map<Character, List<Character>> graph = new HashMap<>();
        graph.put('A', Arrays.asList('B', 'C'));
        graph.put('B', Arrays.asList('D', 'E'));
        graph.put('C', Arrays.asList('F'));
        graph.put('D', new ArrayList<>());
        graph.put('E', Arrays.asList('F'));
        graph.put('F', new ArrayList<>());
        
        System.out.print("BFS遍历: ");
        bfs(graph, 'A');
        // 输出: A B C D E F
    }
}
```

#### 3.5.5 哈希表搜索

**基本思想**：利用哈希函数将键映射到哈希表中的索引位置，实现常数时间的查找

**平均时间复杂度**：O(1)
**最坏时间复杂度**：O(n)，发生在哈希冲突严重的情况下
**空间复杂度**：O(n)
**适用场景**：适用于需要频繁查找、插入和删除操作的场景

**样例代码**：

```java
import java.util.HashMap;
import java.util.Map;

public class HashTableSearch {
    public static Object hashTableSearch(Map<String, Object> hashTable, String key) {
        return hashTable.get(key);
    }

    // 测试
    public static void main(String[] args) {
        // 创建哈希表
        Map<String, Object> hashTable = new HashMap<>();
        hashTable.put("name", "John");
        hashTable.put("age", 30);
        hashTable.put("city", "New York");
        
        System.out.println("查找 'age': " + hashTableSearch(hashTable, "age"));  // 输出: 30
        System.out.println("查找 'salary': " + hashTableSearch(hashTable, "salary"));  // 输出: null
    }
}
```

### 3.6 搜索算法比较

| 搜索算法 | 时间复杂度 | 空间复杂度 | 适用场景 | 特点 |
|---------|-----------|-----------|---------|------|
| 线性搜索 | O(n) | O(1) | 小型或无序数据集 | 实现简单，适用于任何数据结构 |
| 二分搜索 | O(logn) | O(1) | 有序数组 | 效率高，但要求数据有序 |
| DFS | O(V+E) | O(V) | 树和图的遍历 | 可用于寻找路径、检测环 |
| BFS | O(V+E) | O(V) | 树和图的遍历 | 可用于寻找最短路径 |
| 哈希表搜索 | O(1) 平均 | O(n) | 频繁查找场景 | 速度快，但需要额外空间 |

## 4. 递归与分治

### 4.1 递归

#### 4.1.1 基本概念

递归是一种算法设计方法，它通过函数调用自身来解决问题。递归的核心思想是将一个复杂的问题分解为与原问题相似但规模更小的子问题，直到子问题简单到可以直接求解，然后将子问题的解组合起来得到原问题的解。

#### 4.1.2 算法思路

1. **定义递归函数**：确定函数的参数、返回值和功能
2. **确定终止条件**：定义递归的基本情况，当满足这些情况时，递归过程终止
3. **设计递归步骤**：将原问题分解为规模更小的子问题，调用递归函数解决子问题
4. **组合子问题的解**：将子问题的解组合起来，得到原问题的解

#### 4.1.3 优缺点

**优点**：
- 代码简洁，逻辑清晰，易于理解和实现
- 能够自然地表达具有递归结构的问题
- 减少了代码的重复编写

**缺点**：
- 可能导致栈溢出（递归深度过大时）
- 时间和空间效率较低（存在重复计算）
- 调试困难，递归调用栈可能很长

#### 4.1.4 可优化项

1. **记忆化**：缓存中间结果，避免重复计算
2. **尾递归优化**：将递归转换为尾递归形式，减少栈空间的使用
3. **迭代替代**：对于深度过大的递归，考虑使用迭代方式实现
4. **剪枝**：在递归过程中，提前排除不可能的情况
5. **并行化**：对于独立的子问题，考虑使用并行计算

#### 4.1.5 经典样例问题

##### 4.1.5.1 斐波那契数列

**问题描述**：计算第n个斐波那契数

**样例代码**：

```java
public class Fibonacci {
    // 递归实现
    public static int fibonacci(int n) {
        // 终止条件
        if (n <= 1) {
            return n;
        }
        // 递归调用
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    
    // 记忆化优化
    public static int fibonacciMemo(int n) {
        int[] memo = new int[n + 1];
        return fibonacciMemoHelper(n, memo);
    }
    
    private static int fibonacciMemoHelper(int n, int[] memo) {
        if (n <= 1) {
            return n;
        }
        if (memo[n] == 0) {
            memo[n] = fibonacciMemoHelper(n - 1, memo) + fibonacciMemoHelper(n - 2, memo);
        }
        return memo[n];
    }

    // 测试
    public static void main(String[] args) {
        System.out.println("递归实现: " + fibonacci(10));  // 输出: 55
        System.out.println("记忆化优化: " + fibonacciMemo(10));  // 输出: 55
    }
}
```

##### 4.1.5.2 阶乘计算

**问题描述**：计算n的阶乘

**样例代码**：

```java
public class Factorial {
    // 递归实现
    public static int factorial(int n) {
        // 终止条件
        if (n == 0) {
            return 1;
        }
        // 递归调用
        return n * factorial(n - 1);
    }
    
    // 迭代实现
    public static int factorialIterative(int n) {
        int result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    // 测试
    public static void main(String[] args) {
        System.out.println("递归实现: " + factorial(5));  // 输出: 120
        System.out.println("迭代实现: " + factorialIterative(5));  // 输出: 120
    }
}
```

##### 4.1.5.3 二叉树遍历

**问题描述**：实现二叉树的前序、中序和后序遍历

**样例代码**：

```java
import java.util.ArrayList;
import java.util.List;

public class BinaryTreeTraversal {
    // 二叉树节点定义
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int val) {
            this.val = val;
        }
        
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    // 前序遍历（根-左-右）
    public static List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderTraversalHelper(root, result);
        return result;
    }
    
    private static void preorderTraversalHelper(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        result.add(root.val);
        preorderTraversalHelper(root.left, result);
        preorderTraversalHelper(root.right, result);
    }
    
    // 中序遍历（左-根-右）
    public static List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderTraversalHelper(root, result);
        return result;
    }
    
    private static void inorderTraversalHelper(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        inorderTraversalHelper(root.left, result);
        result.add(root.val);
        inorderTraversalHelper(root.right, result);
    }
    
    // 后序遍历（左-右-根）
    public static List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderTraversalHelper(root, result);
        return result;
    }
    
    private static void postorderTraversalHelper(TreeNode root, List<Integer> result) {
        if (root == null) {
            return;
        }
        postorderTraversalHelper(root.left, result);
        postorderTraversalHelper(root.right, result);
        result.add(root.val);
    }

    // 测试
    public static void main(String[] args) {
        // 构建二叉树
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        
        System.out.println("前序遍历: " + preorderTraversal(root));  // 输出: [1, 2, 4, 5, 3]
        System.out.println("中序遍历: " + inorderTraversal(root));    // 输出: [4, 2, 5, 1, 3]
        System.out.println("后序遍历: " + postorderTraversal(root));  // 输出: [4, 5, 2, 3, 1]
    }
}
```

### 4.2 分治

#### 4.2.1 基本概念

分治（Divide and Conquer）是一种算法设计范式，它将原问题分解为若干个规模较小但结构与原问题相似的子问题，递归地解决这些子问题，然后将这些子问题的解组合起来，得到原问题的解。

#### 4.2.2 算法思路

1. **分解（Divide）**：将原问题分解为若干个规模较小、相互独立、与原问题形式相同的子问题
2. **解决（Conquer）**：递归地解决各个子问题。如果子问题规模足够小，则直接求解
3. **合并（Combine）**：将各个子问题的解合并为原问题的解

#### 4.2.3 优缺点

**优点**：
- 能够解决大规模问题，将复杂问题分解为简单子问题
- 分治算法的时间复杂度通常较低，如O(nlogn)
- 适合并行计算，子问题可以独立处理
- 代码结构清晰，易于理解和维护

**缺点**：
- 空间复杂度较高，需要存储子问题的解
- 对于某些问题，合并步骤可能比较复杂
- 分治算法的效率依赖于分解和合并的效率

#### 4.2.4 可优化项

1. **减少递归深度**：对于小规模子问题，使用其他算法（如插入排序）处理
2. **优化合并步骤**：提高合并子问题解的效率
3. **并行化**：利用多核CPU或分布式系统并行处理子问题
4. **内存优化**：减少不必要的内存分配和复制
5. **平衡划分**：确保子问题的规模尽可能均衡，避免最坏情况

#### 4.2.5 经典样例问题

##### 4.2.5.1 归并排序

**问题描述**：对一个数组进行排序

**样例代码**：

```java
public class MergeSort {
    public static int[] mergeSort(int[] arr) {
        // 分解：直到数组长度为1
        if (arr.length <= 1) {
            return arr;
        }
        
        int mid = arr.length / 2;
        int[] left = new int[mid];
        int[] right = new int[arr.length - mid];
        System.arraycopy(arr, 0, left, 0, mid);
        System.arraycopy(arr, mid, right, 0, arr.length - mid);
        
        left = mergeSort(left);  // 递归解决左子问题
        right = mergeSort(right);  // 递归解决右子问题
        
        // 合并：将两个有序数组合并为一个有序数组
        return merge(left, right);
    }
    
    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        int i = 0, j = 0, k = 0;
        
        while (i < left.length && j < right.length) {
            if (left[i] < right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }
        
        while (i < left.length) {
            result[k++] = left[i++];
        }
        while (j < right.length) {
            result[k++] = right[j++];
        }
        return result;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = mergeSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

##### 4.2.5.2 快速排序

**问题描述**：对一个数组进行排序

**样例代码**：

```java
public class QuickSort {
    public static int[] quickSort(int[] arr) {
        // 分解：直到数组长度为1
        if (arr.length <= 1) {
            return arr;
        }
        
        int pivot = arr[arr.length / 2];  // 选择 pivot
        
        // 统计各部分元素数量
        int leftCount = 0, middleCount = 0, rightCount = 0;
        for (int num : arr) {
            if (num < pivot) leftCount++;
            else if (num == pivot) middleCount++;
            else rightCount++;
        }
        
        // 填充各部分数组
        int[] left = new int[leftCount];
        int[] middle = new int[middleCount];
        int[] right = new int[rightCount];
        
        int l = 0, m = 0, r = 0;
        for (int num : arr) {
            if (num < pivot) left[l++] = num;
            else if (num == pivot) middle[m++] = num;
            else right[r++] = num;
        }
        
        // 递归解决子问题
        left = quickSort(left);
        right = quickSort(right);
        
        // 合并结果
        int[] result = new int[arr.length];
        System.arraycopy(left, 0, result, 0, left.length);
        System.arraycopy(middle, 0, result, left.length, middle.length);
        System.arraycopy(right, 0, result, left.length + middle.length, right.length);
        
        return result;
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {64, 34, 25, 12, 22, 11, 90};
        int[] sortedArr = quickSort(arr);
        System.out.print("排序结果: ");
        for (int num : sortedArr) {
            System.out.print(num + " ");
        }
        // 输出: 11 12 22 25 34 64 90
    }
}
```

##### 4.2.5.3 二分搜索

**问题描述**：在有序数组中查找目标元素

**样例代码**：

```java
public class BinarySearch {
    public static int binarySearch(int[] arr, int target, int low, int high) {
        // 终止条件
        if (low > high) {
            return -1;
        }
        
        int mid = low + (high - low) / 2;
        
        if (arr[mid] == target) {
            return mid;
        } else if (arr[mid] < target) {
            // 递归解决右子问题
            return binarySearch(arr, target, mid + 1, high);
        } else {
            // 递归解决左子问题
            return binarySearch(arr, target, low, mid - 1);
        }
    }

    // 测试
    public static void main(String[] args) {
        int[] arr = {11, 12, 22, 25, 34, 64, 90};
        int target = 25;
        int result = binarySearch(arr, target, 0, arr.length - 1);
        System.out.println("元素 " + target + " 在数组中的索引位置: " + result);
        // 输出: 3
    }
}
```

##### 4.2.5.4 大数乘法

**问题描述**：计算两个大数的乘积

**样例代码**：

```python
def multiply(num1, num2):
    # 基本情况
    if len(num1) == 1 or len(num2) == 1:
        return str(int(num1) * int(num2))
    
    n = max(len(num1), len(num2))
    n2 = n // 2
    
    # 分解
    a = num1[:-n2] if len(num1) > n2 else '0'
    b = num1[-n2:] if len(num1) > n2 else num1
    c = num2[:-n2] if len(num2) > n2 else '0'
    d = num2[-n2:] if len(num2) > n2 else num2
    
    # 递归计算
    ac = multiply(a, c)
    bd = multiply(b, d)
    ad_bc = str(int(multiply(a, d)) + int(multiply(b, c)))
    
    # 合并结果
    return str(int(ac) * 10**(2*n2) + int(ad_bc) * 10**n2 + int(bd))

# 测试
num1 = "123456789"
num2 = "987654321"
print(multiply(num1, num2))  # 输出: 121932631112635269
```

### 4.3 递归与分治的关系

递归是分治的实现手段之一，分治是一种算法设计思想。分治算法通常采用递归的方式实现，但递归并不一定是分治。分治强调将问题分解为独立的子问题并合并解，而递归强调函数调用自身的技术。

#### 典型应用
- 排序算法：归并排序、快速排序
- 搜索算法：二分搜索
- 数学问题：大整数乘法、矩阵乘法（Strassen算法）
- 图算法：快速傅里叶变换（FFT）
- 组合问题：排列组合、八皇后问题

## 5. 动态规划

### 5.1 基本概念

动态规划（Dynamic Programming，简称 DP）是一种解决具有重叠子问题和最优子结构特性的问题的算法方法。它通过将原问题分解为相对简单的子问题，先求解子问题，然后从这些子问题的解得到原问题的解。

### 5.2 算法思路

1. **定义状态**：确定问题的状态表示，即如何用变量来描述问题的不同阶段和情况
2. **确定状态转移方程**：找出状态之间的递推关系，即如何从已知状态计算未知状态
3. **初始化边界条件**：确定初始状态的值，即不需要通过其他状态计算就能直接得到的状态值
4. **计算最终结果**：按照状态转移方程的顺序计算所有状态的值，最终得到原问题的解
5. **优化空间复杂度**：根据状态转移的特点，尝试减少所需的存储空间

### 5.3 优缺点

**优点**：
- 能够高效地解决具有重叠子问题和最优子结构的问题
- 时间复杂度通常比暴力枚举低得多
- 可以处理大规模问题
- 代码结构清晰，易于理解和维护

**缺点**：
- 设计动态规划算法需要一定的经验和技巧
- 状态定义和状态转移方程的设计可能比较困难
- 空间复杂度可能较高，需要存储大量状态
- 对于某些问题，动态规划可能不是最优选择

### 5.4 可优化项

1. **空间优化**：使用滚动数组、状态压缩等技术减少空间复杂度
2. **时间优化**：优化状态转移方程，减少计算量
3. **记忆化搜索**：对于某些问题，使用记忆化搜索（递归+缓存）可能更直观
4. **状态定义优化**：选择更合适的状态表示，减少状态数量
5. **并行化**：对于某些问题，考虑使用并行计算

### 5.5 常见问题类型

- **背包问题**：0-1背包、完全背包、多重背包、分组背包
- **序列问题**：最长公共子序列、最长递增子序列、编辑距离
- **路径问题**：最短路径、不同路径、最小路径和
- **矩阵问题**：矩阵链乘法、最大子矩阵
- **分割问题**：分割等和子集、整数拆分
- **股票问题**：买卖股票的最佳时机系列

### 5.6 经典样例问题

#### 5.6.1 斐波那契数列

**问题描述**：计算第n个斐波那契数

**状态定义**：`dp[i]` 表示第i个斐波那契数

**状态转移方程**：`dp[i] = dp[i-1] + dp[i-2]`

**边界条件**：`dp[0] = 0, dp[1] = 1`

**样例代码**：

```java
public class Fibonacci {
    public static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
    
    // 空间优化版本
    public static int fibonacciOptimized(int n) {
        if (n <= 1) {
            return n;
        }
        int a = 0, b = 1;
        for (int i = 2; i <= n; i++) {
            int temp = b;
            b = a + b;
            a = temp;
        }
        return b;
    }

    // 测试
    public static void main(String[] args) {
        System.out.println("普通版本: " + fibonacci(10));  // 输出: 55
        System.out.println("空间优化版本: " + fibonacciOptimized(10));  // 输出: 55
    }
}
```

#### 5.6.2 0-1背包问题

**问题描述**：给定n个物品，每个物品有重量w[i]和价值v[i]，以及一个容量为C的背包。求选择哪些物品放入背包，使得总价值最大。

**状态定义**：`dp[i][j]` 表示前i个物品放入容量为j的背包的最大价值

**状态转移方程**：`dp[i][j] = max(dp[i-1][j], dp[i-1][j-w[i]] + v[i])`（选择第i个物品或不选择）

**边界条件**：`dp[0][j] = 0, dp[i][0] = 0`

**样例代码**：

```java
public class Knapsack01 {
    public static int knapsack01(int[] w, int[] v, int C) {
        int n = w.length;
        int[][] dp = new int[n + 1][C + 1];
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= C; j++) {
                if (j >= w[i - 1]) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][j - w[i - 1]] + v[i - 1]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        
        return dp[n][C];
    }
    
    // 空间优化版本
    public static int knapsack01Optimized(int[] w, int[] v, int C) {
        int n = w.length;
        int[] dp = new int[C + 1];
        
        for (int i = 0; i < n; i++) {
            for (int j = C; j >= w[i]; j--) {
                dp[j] = Math.max(dp[j], dp[j - w[i]] + v[i]);
            }
        }
        
        return dp[C];
    }

    // 测试
    public static void main(String[] args) {
        int[] w = {10, 20, 30};
        int[] v = {60, 100, 120};
        int C = 50;
        System.out.println("普通版本: " + knapsack01(w, v, C));  // 输出: 220
        System.out.println("空间优化版本: " + knapsack01Optimized(w, v, C));  // 输出: 220
    }
}
```

#### 5.6.3 最长递增子序列

**问题描述**：给定一个整数数组，求其中最长递增子序列的长度。

**状态定义**：`dp[i]` 表示以第i个元素结尾的最长递增子序列的长度

**状态转移方程**：`dp[i] = max(dp[j] + 1) for j < i and nums[j] < nums[i]`

**边界条件**：`dp[i] = 1`（每个元素自身构成一个长度为1的子序列）

**样例代码**：

```java
import java.util.ArrayList;
import java.util.Arrays;

public class LongestIncreasingSubsequence {
    public static int lengthOfLIS(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
        }
        
        int maxLength = 0;
        for (int length : dp) {
            maxLength = Math.max(maxLength, length);
        }
        return maxLength;
    }
    
    // 二分查找优化版本（O(nlogn)）
    public static int lengthOfLISOptimized(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        ArrayList<Integer> tails = new ArrayList<>();
        for (int num : nums) {
            int left = 0, right = tails.size();
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails.get(mid) < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            if (left == tails.size()) {
                tails.add(num);
            } else {
                tails.set(left, num);
            }
        }
        return tails.size();
    }

    // 测试
    public static void main(String[] args) {
        int[] nums = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("普通版本: " + lengthOfLIS(nums));  // 输出: 4
        System.out.println("二分查找优化版本: " + lengthOfLISOptimized(nums));  // 输出: 4
    }
}
```

#### 5.6.4 最长公共子序列

**问题描述**：给定两个字符串，求它们的最长公共子序列的长度。

**状态定义**：`dp[i][j]` 表示字符串s1的前i个字符和字符串s2的前j个字符的最长公共子序列长度

**状态转移方程**：
- 如果s1[i-1] == s2[j-1]，则 `dp[i][j] = dp[i-1][j-1] + 1`
- 否则，`dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

**边界条件**：`dp[i][0] = 0, dp[0][j] = 0`

**样例代码**：

```java
public class LongestCommonSubsequence {
    public static int longestCommonSubsequence(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }

    // 测试
    public static void main(String[] args) {
        String s1 = "abcde";
        String s2 = "ace";
        System.out.println("最长公共子序列长度: " + longestCommonSubsequence(s1, s2));  // 输出: 3
    }
}
```

#### 5.6.5 爬楼梯

**问题描述**：有n阶楼梯，每次可以爬1或2阶，求有多少种不同的方法爬到楼顶。

**状态定义**：`dp[i]` 表示爬到第i阶的不同方法数

**状态转移方程**：`dp[i] = dp[i-1] + dp[i-2]`（最后一步爬1阶或2阶）

**边界条件**：`dp[1] = 1, dp[2] = 2`

**样例代码**：

```java
public class ClimbStairs {
    public static int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
    
    // 空间优化版本
    public static int climbStairsOptimized(int n) {
        if (n == 1) {
            return 1;
        }
        int a = 1, b = 2;
        for (int i = 3; i <= n; i++) {
            int temp = b;
            b = a + b;
            a = temp;
        }
        return b;
    }

    // 测试
    public static void main(String[] args) {
        System.out.println("普通版本: " + climbStairs(5));  // 输出: 8
        System.out.println("空间优化版本: " + climbStairsOptimized(5));  // 输出: 8
    }
}
```

### 5.7 核心原理

#### 1. 最优子结构
一个问题的最优解包含其子问题的最优解。也就是说，我们可以通过子问题的最优解来构造原问题的最优解。

#### 2. 重叠子问题
在解决原问题的过程中，我们需要多次求解相同的子问题。动态规划通过存储子问题的解来避免重复计算，从而提高效率。

#### 3. 状态转移方程
描述问题状态之间关系的数学公式，它定义了如何从子问题的解得到原问题的解。

#### 4. 记忆化搜索
将已经计算过的子问题的解存储起来，当再次需要计算时直接取用，避免重复计算。

### 5.8 解题步骤

1. **定义状态**：确定问题的状态表示，即如何用变量来描述问题的不同阶段和情况
2. **确定状态转移方程**：找出状态之间的递推关系，即如何从已知状态计算未知状态
3. **初始化边界条件**：确定初始状态的值，即不需要通过其他状态计算就能直接得到的状态值
4. **计算最终结果**：按照状态转移方程的顺序计算所有状态的值，最终得到原问题的解
5. **优化空间复杂度**：根据状态转移的特点，尝试减少所需的存储空间

### 5.9 常见问题类型

- **背包问题**：0-1背包、完全背包、多重背包、分组背包
- **序列问题**：最长公共子序列、最长递增子序列、编辑距离
- **路径问题**：最短路径、不同路径、最小路径和
- **矩阵问题**：矩阵链乘法、最大子矩阵
- **分割问题**：分割等和子集、整数拆分
- **股票问题**：买卖股票的最佳时机系列

### 5.10 样例代码

#### 1. 斐波那契数列（基础DP）

**问题描述**：计算第n个斐波那契数

**状态定义**：`dp[i]` 表示第i个斐波那契数

**状态转移方程**：`dp[i] = dp[i-1] + dp[i-2]`

**边界条件**：`dp[0] = 0, dp[1] = 1`

```python
def fibonacci(n):
    if n <= 1:
        return n
    dp = [0] * (n + 1)
    dp[0] = 0
    dp[1] = 1
    for i in range(2, n + 1):
        dp[i] = dp[i-1] + dp[i-2]
    return dp[n]

# 空间优化版本
def fibonacci_optimized(n):
    if n <= 1:
        return n
    a, b = 0, 1
    for _ in range(2, n + 1):
        a, b = b, a + b
    return b
```

#### 2. 0-1背包问题

**问题描述**：给定n个物品，每个物品有重量w[i]和价值v[i]，以及一个容量为C的背包。求选择哪些物品放入背包，使得总价值最大。

**状态定义**：`dp[i][j]` 表示前i个物品放入容量为j的背包的最大价值

**状态转移方程**：`dp[i][j] = max(dp[i-1][j], dp[i-1][j-w[i]] + v[i])`（选择第i个物品或不选择）

**边界条件**：`dp[0][j] = 0, dp[i][0] = 0`

```python
def knapsack_01(w, v, C):
    n = len(w)
    dp = [[0] * (C + 1) for _ in range(n + 1)]
    
    for i in range(1, n + 1):
        for j in range(1, C + 1):
            if j >= w[i-1]:
                dp[i][j] = max(dp[i-1][j], dp[i-1][j - w[i-1]] + v[i-1])
            else:
                dp[i][j] = dp[i-1][j]
    
    return dp[n][C]

# 空间优化版本
def knapsack_01_optimized(w, v, C):
    n = len(w)
    dp = [0] * (C + 1)
    
    for i in range(n):
        for j in range(C, w[i] - 1, -1):
            dp[j] = max(dp[j], dp[j - w[i]] + v[i])
    
    return dp[C]
```

#### 3. 最长递增子序列

**问题描述**：给定一个整数数组，求其中最长递增子序列的长度。

**状态定义**：`dp[i]` 表示以第i个元素结尾的最长递增子序列的长度

**状态转移方程**：`dp[i] = max(dp[j] + 1) for j < i and nums[j] < nums[i]`

**边界条件**：`dp[i] = 1`（每个元素自身构成一个长度为1的子序列）

```python
def length_of_lis(nums):
    if not nums:
        return 0
    n = len(nums)
    dp = [1] * n
    
    for i in range(1, n):
        for j in range(i):
            if nums[j] < nums[i]:
                dp[i] = max(dp[i], dp[j] + 1)
    
    return max(dp)

# 二分查找优化版本（O(nlogn)）
def length_of_lis_optimized(nums):
    tails = []
    for num in nums:
        left, right = 0, len(tails)
        while left < right:
            mid = (left + right) // 2
            if tails[mid] < num:
                left = mid + 1
            else:
                right = mid
        if left == len(tails):
            tails.append(num)
        else:
            tails[left] = num
    return len(tails)
```

#### 4. 最长公共子序列

**问题描述**：给定两个字符串，求它们的最长公共子序列的长度。

**状态定义**：`dp[i][j]` 表示字符串s1的前i个字符和字符串s2的前j个字符的最长公共子序列长度

**状态转移方程**：
- 如果s1[i-1] == s2[j-1]，则 `dp[i][j] = dp[i-1][j-1] + 1`
- 否则，`dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

**边界条件**：`dp[i][0] = 0, dp[0][j] = 0`

```python
def longest_common_subsequence(s1, s2):
    m, n = len(s1), len(s2)
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if s1[i-1] == s2[j-1]:
                dp[i][j] = dp[i-1][j-1] + 1
            else:
                dp[i][j] = max(dp[i-1][j], dp[i][j-1])
    
    return dp[m][n]
```

#### 5. 爬楼梯问题

**问题描述**：有n阶楼梯，每次可以爬1或2阶，求有多少种不同的方法爬到楼顶。

**状态定义**：`dp[i]` 表示爬到第i阶的不同方法数

**状态转移方程**：`dp[i] = dp[i-1] + dp[i-2]`（最后一步爬1阶或2阶）

**边界条件**：`dp[1] = 1, dp[2] = 2`

```python
def climb_stairs(n):
    if n <= 2:
        return n
    dp = [0] * (n + 1)
    dp[1] = 1
    dp[2] = 2
    for i in range(3, n + 1):
        dp[i] = dp[i-1] + dp[i-2]
    return dp[n]

# 空间优化版本
def climb_stairs_optimized(n):
    if n <= 2:
        return n
    a, b = 1, 2
    for _ in range(3, n + 1):
        a, b = b, a + b
    return b
```

### 5.12 动态规划的优化技巧

1. **空间优化**：利用滚动数组、状态压缩等技术减少空间使用
2. **状态转移优化**：通过数学分析或数据结构（如单调队列、线段树）优化状态转移过程
3. **维度优化**：将高维状态转换为低维状态
4. **记忆化搜索**：对于一些状态转移顺序不明显的问题，使用记忆化搜索（递归+缓存）的方式实现
5. **表格填充顺序**：根据状态转移的依赖关系，确定正确的计算顺序

### 5.13 动态规划与其他算法的比较

| 算法 | 适用场景 | 时间复杂度 | 空间复杂度 | 特点 |
|------|---------|-----------|-----------|------|
| 动态规划 | 有重叠子问题和最优子结构的问题 | 通常为O(n²)或O(n³) | 通常为O(n²)，可优化 | 避免重复计算，效率高 |
| 贪心算法 | 具有贪心选择性质的问题 | 通常为O(n)或O(nlogn) | 通常为O(1) | 局部最优->全局最优，实现简单 |
| 分治算法 | 可分解为独立子问题的问题 | 通常为O(nlogn) | 通常为O(n) | 递归求解，子问题独立 |
| 暴力枚举 | 所有可能情况 | 通常为O(2^n)或O(n!) | 通常为O(n) | 实现简单，但效率低 |

## 6. 暴力算法

### 6.1 基本概念

暴力算法（Brute Force）是一种直接、简单的算法设计思想，它通过枚举所有可能的情况来求解问题。暴力算法不考虑优化，直接按照问题的描述进行求解，通常是解决问题的最直接方法。

### 6.2 算法思路

1. **枚举所有可能**：生成所有可能的解决方案
2. **验证每个方案**：检查每个方案是否满足问题的要求
3. **选择最优解**：从满足要求的方案中选择最优解（如果需要）

### 6.3 优缺点

**优点**：
- 实现简单，逻辑清晰
- 不需要复杂的算法设计
- 对于小规模问题，效率可以接受
- 可以作为其他优化算法的基准

**缺点**：
- 时间复杂度高，通常为指数级或阶乘级
- 对于大规模问题，效率低下，甚至无法在合理时间内完成
- 空间复杂度可能较高，需要存储所有可能的解决方案

### 6.4 可优化项

1. **剪枝**：在枚举过程中，提前排除不可能的情况
2. **预处理**：对输入数据进行预处理，减少枚举的范围
3. **利用问题特性**：根据问题的特性，减少枚举的维度或数量
4. **缓存**：缓存中间结果，避免重复计算
5. **并行计算**：利用多线程或分布式计算，加速枚举过程

### 6.5 经典样例问题

#### 6.5.1 两数之和

**问题描述**：给定一个整数数组 `nums` 和一个目标值 `target`，请你在该数组中找出和为目标值的那两个整数，并返回它们的数组下标。

**样例代码**：

```python
def two_sum(nums, target):
    n = len(nums)
    for i in range(n):
        for j in range(i + 1, n):
            if nums[i] + nums[j] == target:
                return [i, j]
    return []

# 测试
nums = [2, 7, 11, 15]
target = 9
print(two_sum(nums, target))  # 输出: [0, 1]
```

#### 6.5.2 全排列

**问题描述**：给定一个没有重复数字的序列，返回其所有可能的全排列。

**样例代码**：

```python
def permute(nums):
    def backtrack(path, used):
        if len(path) == len(nums):
            result.append(path[:])
            return
        for i in range(len(nums)):
            if used[i]:
                continue
            path.append(nums[i])
            used[i] = True
            backtrack(path, used)
            path.pop()
            used[i] = False
    
    result = []
    backtrack([], [False] * len(nums))
    return result

# 测试
nums = [1, 2, 3]
print(permute(nums))  # 输出: [[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]
```

#### 6.5.3 子集和问题

**问题描述**：给定一个正整数数组和一个目标值，判断数组中是否存在一组数，其和等于目标值。

**样例代码**：

```python
def subset_sum(nums, target):
    n = len(nums)
    # 生成所有可能的子集
    for i in range(1, 1 << n):
        current_sum = 0
        for j in range(n):
            if i & (1 << j):
                current_sum += nums[j]
        if current_sum == target:
            return True
    return False

# 测试
nums = [3, 34, 4, 12, 5, 2]
target = 9
print(subset_sum(nums, target))  # 输出: True (3 + 4 + 2 = 9)
```

## 7. 贪心算法

### 7.1 基本概念

贪心算法（Greedy Algorithm）是一种在每一步选择中都采取在当前状态下最好或最优（即最有利）的选择，从而希望导致结果是全局最好或最优的算法。

### 7.2 算法思路

1. **确定贪心策略**：选择一个贪心策略，即如何在每一步做出局部最优选择
2. **证明贪心选择性质**：证明每一步的局部最优选择能够导致全局最优解
3. **应用贪心策略**：按照贪心策略，逐步构建解决方案
4. **验证解决方案**：检查构建的解决方案是否满足问题的要求

### 7.3 优缺点

**优点**：
- 实现简单，代码量少
- 时间复杂度低，通常为线性或线性对数时间
- 空间复杂度低，通常为常数空间

**缺点**：
- 不是所有问题都适合使用贪心算法
- 需要证明贪心选择性质，否则可能得到次优解
- 对于某些问题，贪心算法可能无法找到正确的解

### 7.4 可优化项

1. **选择合适的贪心策略**：不同的贪心策略可能导致不同的结果
2. **预处理数据**：对输入数据进行排序或其他预处理，以便应用贪心策略
3. **结合其他算法**：对于复杂问题，可能需要结合动态规划、分治等其他算法
4. **处理边界情况**：确保贪心策略在边界情况下也能正确工作

### 7.5 经典样例问题

#### 7.5.1 活动选择问题

**问题描述**：给定一组活动，每个活动有开始时间和结束时间，选择最多的互不重叠的活动。

**样例代码**：

```python
def activity_selection(activities):
    # 按结束时间排序
    activities.sort(key=lambda x: x[1])
    selected = []
    last_end = -1
    
    for activity in activities:
        start, end = activity
        if start >= last_end:
            selected.append(activity)
            last_end = end
    
    return selected

# 测试
activities = [(1, 4), (3, 5), (0, 6), (5, 7), (3, 9), (5, 9), (6, 10), (8, 11), (8, 12), (2, 14), (12, 16)]
print(activity_selection(activities))  # 输出: [(1, 4), (5, 7), (8, 11), (12, 16)]
```

#### 7.5.2 零钱兑换问题

**问题描述**：给定不同面额的硬币和一个总金额，计算使用最少的硬币数来组成总金额。假设每种硬币的数量是无限的。

**样例代码**：

```python
def coin_change(coins, amount):
    # 按面额从大到小排序
    coins.sort(reverse=True)
    min_coins = float('inf')
    
    def backtrack(remaining, coin_count, index):
        nonlocal min_coins
        if remaining == 0:
            min_coins = min(min_coins, coin_count)
            return
        if index >= len(coins):
            return
        if coin_count >= min_coins:
            return
        
        # 尽可能多地使用当前面额的硬币
        max_usage = remaining // coins[index]
        for i in range(max_usage, -1, -1):
            backtrack(remaining - i * coins[index], coin_count + i, index + 1)
    
    backtrack(amount, 0, 0)
    return min_coins if min_coins != float('inf') else -1

# 测试
coins = [1, 2, 5]
amount = 11
print(coin_change(coins, amount))  # 输出: 3 (5 + 5 + 1)
```

#### 7.5.3 跳跃游戏

**问题描述**：给定一个非负整数数组，你最初位于数组的第一个位置。数组中的每个元素代表你在该位置可以跳跃的最大长度。判断你是否能够到达最后一个位置。

**样例代码**：

```python
def can_jump(nums):
    max_reach = 0
    for i in range(len(nums)):
        if i > max_reach:
            return False
        max_reach = max(max_reach, i + nums[i])
        if max_reach >= len(nums) - 1:
            return True
    return False

# 测试
nums = [2, 3, 1, 1, 4]
print(can_jump(nums))  # 输出: True

nums = [3, 2, 1, 0, 4]
print(can_jump(nums))  # 输出: False
```

## 8. 回溯算法

### 8.1 基本概念

回溯算法（Backtracking）是一种通过探索所有可能的候选解来找出所有解的算法。如果候选解被确认不是一个解（或者至少不是最后一个解），回溯算法会通过在上一步进行一些变化来舍弃该解，即回溯并且尝试另一种可能。

### 8.2 算法思路

1. **选择**：在当前状态下，选择一个可能的选项
2. **探索**：递归地探索该选项的所有可能结果
3. **回溯**：如果该选项不能导致有效解，撤销该选择，回到之前的状态，尝试其他选项
4. **剪枝**：在探索过程中，提前排除不可能导致有效解的选项

### 8.3 优缺点

**优点**：
- 可以找到所有可能的解决方案
- 对于约束满足问题，非常有效
- 可以通过剪枝优化，提高效率

**缺点**：
- 时间复杂度高，通常为指数级或阶乘级
- 空间复杂度高，需要存储递归栈和中间结果
- 对于大规模问题，效率低下

### 8.4 可优化项

1. **剪枝**：提前排除不可能导致有效解的选项
2. **记忆化**：缓存中间结果，避免重复计算
3. **启发式搜索**：使用启发式策略，优先探索更可能导致有效解的选项
4. **迭代实现**：使用迭代代替递归，减少栈溢出的风险

### 8.5 经典样例问题

#### 8.5.1 N皇后问题

**问题描述**：在n×n的棋盘上放置n个皇后，使得它们不能互相攻击（即任意两个皇后不能处于同一行、同一列或同一斜线上）。

**样例代码**：

```python
def solve_n_queens(n):
    def is_valid(board, row, col):
        # 检查列
        for i in range(row):
            if board[i][col] == 'Q':
                return False
        # 检查左上到右下的斜线
        i, j = row - 1, col - 1
        while i >= 0 and j >= 0:
            if board[i][j] == 'Q':
                return False
            i -= 1
            j -= 1
        # 检查右上到左下的斜线
        i, j = row - 1, col + 1
        while i >= 0 and j < n:
            if board[i][j] == 'Q':
                return False
            i -= 1
            j += 1
        return True
    
    def backtrack(row, board):
        if row == n:
            result.append([''.join(row) for row in board])
            return
        for col in range(n):
            if is_valid(board, row, col):
                board[row][col] = 'Q'
                backtrack(row + 1, board)
                board[row][col] = '.'
    
    result = []
    board = [['.' for _ in range(n)] for _ in range(n)]
    backtrack(0, board)
    return result

# 测试
n = 4
print(solve_n_queens(n))  # 输出: [[".Q..", "...Q", "Q...", "..Q."], ["..Q.", "Q...", "...Q", ".Q.."]]
```

#### 8.5.2 数独求解

**问题描述**：解决一个数独问题，使得每行、每列和每个3×3的子网格都包含1-9的数字，且不重复。

**样例代码**：

```python
def solve_sudoku(board):
    def is_valid(row, col, num):
        # 检查行
        for i in range(9):
            if board[row][i] == num:
                return False
        # 检查列
        for i in range(9):
            if board[i][col] == num:
                return False
        # 检查3x3子网格
        box_row = (row // 3) * 3
        box_col = (col // 3) * 3
        for i in range(3):
            for j in range(3):
                if board[box_row + i][box_col + j] == num:
                    return False
        return True
    
    def backtrack():
        for i in range(9):
            for j in range(9):
                if board[i][j] == '.':
                    for num in '123456789':
                        if is_valid(i, j, num):
                            board[i][j] = num
                            if backtrack():
                                return True
                            board[i][j] = '.'
                    return False
        return True
    
    backtrack()
    return board

# 测试
board = [
    ["5", "3", ".", ".", "7", ".", ".", ".", "."],
    ["6", ".", ".", "1", "9", "5", ".", ".", "."],
    [".", "9", "8", ".", ".", ".", ".", "6", "."],
    ["8", ".", ".", ".", "6", ".", ".", ".", "3"],
    ["4", ".", ".", "8", ".", "3", ".", ".", "1"],
    ["7", ".", ".", ".", "2", ".", ".", ".", "6"],
    [".", "6", ".", ".", ".", ".", "2", "8", "."],
    [".", ".", ".", "4", "1", "9", ".", ".", "5"],
    [".", ".", ".", ".", "8", ".", ".", "7", "9"]
]
print(solve_sudoku(board))
```

#### 8.5.3 组合总和

**问题描述**：给定一个无重复元素的数组和一个目标值，找出所有可以使数字和为目标值的组合。数组中的数字可以无限制重复使用。

**样例代码**：

```python
def combination_sum(candidates, target):
    def backtrack(start, path, current_sum):
        if current_sum == target:
            result.append(path[:])
            return
        if current_sum > target:
            return
        for i in range(start, len(candidates)):
            path.append(candidates[i])
            backtrack(i, path, current_sum + candidates[i])
            path.pop()
    
    result = []
    backtrack(0, [], 0)
    return result

# 测试
candidates = [2, 3, 6, 7]
target = 7
print(combination_sum(candidates, target))  # 输出: [[2, 2, 3], [7]]
```

## 9. 分支限界算法

### 9.1 基本概念

分支限界算法（Branch and Bound）是一种用于求解组合优化问题的算法，它通过系统地搜索问题的解空间树，利用上下界剪枝来减少搜索范围，从而找到最优解。

### 9.2 算法思路

1. **生成解空间树**：将问题的所有可能解组织成一棵树
2. **计算上下界**：对每个节点，计算目标函数的上下界
3. **优先级队列**：使用优先级队列（如最小堆或最大堆）来选择下一个要探索的节点
4. **剪枝**：如果一个节点的下界（对于最小化问题）大于当前已知的最优解，则剪枝该节点
5. **找到最优解**：当队列为空时，当前已知的最优解就是问题的最优解

### 9.3 优缺点

**优点**：
- 可以找到最优解
- 相比暴力枚举，效率更高
- 对于某些问题，如旅行商问题、背包问题等，非常有效

**缺点**：
- 实现复杂，需要计算上下界
- 对于大规模问题，仍然可能效率低下
- 空间复杂度高，需要存储解空间树的节点

### 9.4 可优化项

1. **选择合适的优先级队列**：根据问题的特性，选择合适的优先级队列
2. **优化上下界计算**：更准确的上下界可以减少搜索范围
3. **剪枝策略**：使用更有效的剪枝策略
4. **并行计算**：利用多线程或分布式计算，加速搜索过程

### 9.5 经典样例问题

#### 9.5.1 旅行商问题

**问题描述**：给定一系列城市和每对城市之间的距离，找到一条最短路径，使得旅行者访问每个城市恰好一次并返回起始城市。

**样例代码**：

```python
import heapq

def travelingsalesmanproblem(distances):
    n = len(distances)
    # 状态: (当前成本, 当前城市, 已访问的城市)
    # 使用优先队列，按当前成本排序
    heap = []
    # 从城市0出发，已访问城市0
    heapq.heappush(heap, (0, 0, frozenset([0])))
    
    min_cost = float('inf')
    
    while heap:
        current_cost, current_city, visited = heapq.heappop(heap)
        
        # 如果所有城市都已访问，返回起始城市
        if len(visited) == n:
            total_cost = current_cost + distances[current_city][0]
            if total_cost < min_cost:
                min_cost = total_cost
            continue
        
        # 剪枝：如果当前成本已经大于已知的最小成本，跳过
        if current_cost >= min_cost:
            continue
        
        # 探索下一个城市
        for next_city in range(n):
            if next_city not in visited:
                new_cost = current_cost + distances[current_city][next_city]
                # 剪枝：如果新成本已经大于已知的最小成本，跳过
                if new_cost < min_cost:
                    new_visited = visited.union({next_city})
                    heapq.heappush(heap, (new_cost, next_city, new_visited))
    
    return min_cost

# 测试
distances = [
    [0, 10, 15, 20],
    [10, 0, 35, 25],
    [15, 35, 0, 30],
    [20, 25, 30, 0]
]
print(travelingsalesmanproblem(distances))  # 输出: 80
```

#### 9.5.2 0-1背包问题

**问题描述**：给定一组物品，每个物品有重量和价值，在背包容量有限的情况下，选择哪些物品放入背包，使得总价值最大。

**样例代码**：

```python
import heapq

def knapsack_01_branch_and_bound(values, weights, capacity):
    n = len(values)
    # 计算价值重量比，用于排序
    items = sorted(range(n), key=lambda i: values[i]/weights[i], reverse=True)
    
    max_value = 0
    
    # 节点: (负的当前价值下界, 当前价值, 当前重量, 当前物品索引)
    # 使用最大堆，按负的下界排序（因为heapq是最小堆）
    heap = []
    # 初始节点：还没有选择任何物品
    heapq.heappush(heap, (0, 0, 0, 0))
    
    while heap:
        neg_bound, current_value, current_weight, index = heapq.heappop(heap)
        bound = -neg_bound
        
        # 更新最大价值
        if current_value > max_value:
            max_value = current_value
        
        # 如果已经处理完所有物品，跳过
        if index == n:
            continue
        
        # 剪枝：如果当前下界小于等于已知的最大价值，跳过
        if bound <= max_value:
            continue
        
        # 计算不选当前物品的情况
        # 计算下界：当前价值 + 剩余物品的最大可能价值
        next_index = index + 1
        next_weight = current_weight
        next_value = current_value
        # 计算下界
        bound = next_value
        remaining_capacity = capacity - next_weight
        for i in range(next_index, n):
            item = items[i]
            if remaining_capacity >= weights[item]:
                bound += values[item]
                remaining_capacity -= weights[item]
            else:
                bound += values[item] * (remaining_capacity / weights[item])
                break
        # 如果下界大于当前最大价值，加入堆
        if bound > max_value:
            heapq.heappush(heap, (-bound, next_value, next_weight, next_index))
        
        # 计算选当前物品的情况
        item = items[index]
        next_weight = current_weight + weights[item]
        if next_weight <= capacity:
            next_value = current_value + values[item]
            # 计算下界
            bound = next_value
            remaining_capacity = capacity - next_weight
            for i in range(next_index, n):
                item = items[i]
                if remaining_capacity >= weights[item]:
                    bound += values[item]
                    remaining_capacity -= weights[item]
                else:
                    bound += values[item] * (remaining_capacity / weights[item])
                    break
            # 如果下界大于当前最大价值，加入堆
            if bound > max_value:
                heapq.heappush(heap, (-bound, next_value, next_weight, next_index))
    
    return max_value

# 测试
values = [60, 100, 120]
weights = [10, 20, 30]
capacity = 50
print(knapsack_01_branch_and_bound(values, weights, capacity))  # 输出: 220
```

#### 9.5.3 最短路径问题

**问题描述**：给定一个带权有向图，找到从起始节点到目标节点的最短路径。

**样例代码**：

```python
import heapq

def shortest_path(graph, start, end):
    # 使用优先队列，按距离排序
    heap = []
    heapq.heappush(heap, (0, start, [start]))
    # 记录已访问的节点及其最短距离
    visited = {}
    
    while heap:
        current_distance, current_node, path = heapq.heappop(heap)
        
        # 如果到达目标节点，返回路径和距离
        if current_node == end:
            return path, current_distance
        
        # 如果当前节点已经访问过，且当前距离大于已记录的最短距离，跳过
        if current_node in visited and current_distance >= visited[current_node]:
            continue
        
        # 记录当前节点的最短距离
        visited[current_node] = current_distance
        
        # 探索邻居节点
        for neighbor, weight in graph[current_node].items():
            distance = current_distance + weight
            # 如果邻居节点未访问过，或者当前距离小于已记录的距离，加入堆
            if neighbor not in visited or distance < visited.get(neighbor, float('inf')):
                heapq.heappush(heap, (distance, neighbor, path + [neighbor]))
    
    # 如果无法到达目标节点
    return None, float('inf')

# 测试
graph = {
    'A': {'B': 1, 'C': 4},
    'B': {'A': 1, 'C': 2, 'D': 5},
    'C': {'A': 4, 'B': 2, 'D': 1},
    'D': {'B': 5, 'C': 1}
}
start = 'A'
end = 'D'
path, distance = shortest_path(graph, start, end)
print(f"最短路径: {path}, 距离: {distance}")  # 输出: 最短路径: ['A', 'B', 'C', 'D'], 距离: 4
```

## 10. 数据结构

### 10.1 线性结构

1. **数组**
   - 优点：随机访问速度快
   - 缺点：插入删除操作效率低

2. **链表**
   - 优点：插入删除操作效率高
   - 缺点：随机访问速度慢
   - 类型：单链表、双链表、循环链表

3. **栈**
   - 特点：后进先出 (LIFO)
   - 应用：表达式求值、括号匹配、函数调用栈

4. **队列**
   - 特点：先进先出 (FIFO)
   - 应用：广度优先搜索、任务调度

### 10.2 非线性结构

1. **树**
   - **二叉树**：每个节点最多有两个子节点
   - **二叉搜索树**：左子树所有节点值小于根节点，右子树所有节点值大于根节点
   - **平衡二叉树**：左右子树高度差不超过1
   - **红黑树**：一种自平衡的二叉搜索树
   - **B树**：多路搜索树，常用于数据库索引

2. **图**
   - **有向图**：边有方向
   - **无向图**：边无方向
   - **带权图**：边有权重
   - **应用**：社交网络、路由算法、最短路径

3. **哈希表**
   - 特点：通过哈希函数实现键值对的快速查找
   - 应用：缓存、字典、集合

### 10.3 跳跃表

#### 基本概念

跳跃表（Skip List）是一种有序数据结构，通过在每个节点中维护多个指向其他节点的指针，从而达到快速访问节点的目的。它结合了链表的灵活性和二分查找的高效性，是一种空间换时间的典型应用。

#### 基本结构

1. **多层链表**：跳跃表由多层链表组成，每一层都是一个有序链表
2. **层数**：通过随机函数决定每个节点的层数，通常遵循几何分布
3. **最底层**：包含所有元素的完整链表
4. **上层结构**：每一层都是下层的"快速通道"，节点数逐渐减少
5. **节点结构**：每个节点包含一个值和多个指针，指针分别指向同一层的下一个节点和下一层的对应节点

#### 跳跃表的优势

- **查找效率**：平均时间复杂度O(logn)
- **插入删除**：平均时间复杂度O(logn)
- **实现简单**：相比平衡树，实现更简单，代码可读性高
- **空间复杂度**：O(n)，但实际使用中通常比平衡树节省空间
- **范围查询**：支持高效的范围查询操作
- **并发友好**：相比平衡树，更容易实现并发操作

#### 数据结构图

```
Level 4: 1 ----------------------------> 10
          |                              |
Level 3: 1 ----------> 5 ---------------> 10
          |            |                 |
Level 2: 1 --> 3 --> 5 --> 7 ----------> 10
          |     |     |     |           |
Level 1: 1 --> 3 --> 5 --> 7 --> 9 --> 10
```

#### 基本操作过程

##### 1. 查找操作

1. **从顶层开始**：从跳跃表的最高层开始查找
2. **向右比较**：在当前层，向右比较节点值，直到找到大于或等于目标值的节点
3. **向下移动**：如果当前节点值大于目标值，或已到达当前层末尾，则向下移动一层
4. **重复过程**：重复步骤2-3，直到到达最底层
5. **找到目标**：在最底层，向右查找，若找到目标值则返回，否则返回未找到

**查找过程示例**：查找值为7的节点
- 从Level 4开始，1 < 7，向右移动到10
- 10 > 7，向下移动到Level 3
- 5 < 7，向右移动到10
- 10 > 7，向下移动到Level 2
- 5 < 7，向右移动到7，找到目标

##### 2. 插入操作

1. **查找插入位置**：使用类似查找的过程，找到在各层中需要插入的位置
2. **随机决定层数**：为新节点随机生成一个层数
3. **调整跳跃表高度**：如果新节点的层数超过当前跳跃表的高度，则增加跳跃表的高度
4. **创建新节点**：创建包含随机层数的新节点
5. **插入节点**：在各层的对应位置插入新节点，并更新相关指针

##### 3. 删除操作

1. **查找目标节点**：使用查找操作找到目标节点
2. **更新指针**：从最低层开始，逐层更新指向被删除节点的指针，使其跳过被删除节点
3. **调整跳跃表高度**：如果删除节点后，最高层变为空，则降低跳跃表的高度
4. **释放节点**：释放被删除节点的内存

#### 样例代码

```python
import random

class SkipNode:
    def __init__(self, value, level):
        self.value = value
        self.forward = [None] * (level + 1)  # 存储各层的下一个节点

class SkipList:
    def __init__(self, max_level=4):
        self.max_level = max_level
        self.level = 0  # 当前跳跃表的最大层数
        self.header = SkipNode(-1, max_level)  # 头节点，值为-1
    
    def random_level(self):
        """随机生成节点的层数，遵循几何分布"""
        level = 0
        while random.random() < 0.5 and level < self.max_level:
            level += 1
        return level
    
    def insert(self, value):
        """插入新值"""
        # 用于存储每一层需要更新的节点
        update = [None] * (self.max_level + 1)
        current = self.header
        
        # 从最高层开始查找插入位置
        for i in range(self.level, -1, -1):
            while current.forward[i] and current.forward[i].value < value:
                current = current.forward[i]
            update[i] = current
        
        # 随机生成新节点的层数
        new_level = self.random_level()
        
        # 如果新节点的层数超过当前跳跃表的高度，则更新跳跃表的高度
        if new_level > self.level:
            for i in range(self.level + 1, new_level + 1):
                update[i] = self.header
            self.level = new_level
        
        # 创建新节点并插入
        new_node = SkipNode(value, new_level)
        for i in range(new_level + 1):
            new_node.forward[i] = update[i].forward[i]
            update[i].forward[i] = new_node
    
    def search(self, value):
        """查找值"""
        current = self.header
        
        # 从最高层开始查找
        for i in range(self.level, -1, -1):
            while current.forward[i] and current.forward[i].value < value:
                current = current.forward[i]
        
        # 移动到下一个节点，判断是否找到
        current = current.forward[0]
        if current and current.value == value:
            return True
        return False
    
    def delete(self, value):
        """删除值"""
        # 用于存储每一层需要更新的节点
        update = [None] * (self.max_level + 1)
        current = self.header
        
        # 从最高层开始查找
        for i in range(self.level, -1, -1):
            while current.forward[i] and current.forward[i].value < value:
                current = current.forward[i]
            update[i] = current
        
        # 移动到下一个节点，判断是否找到
        current = current.forward[0]
        if current and current.value == value:
            # 从最低层开始删除
            for i in range(self.level + 1):
                if update[i].forward[i] != current:
                    break
                update[i].forward[i] = current.forward[i]
            
            # 调整跳跃表的高度
            while self.level > 0 and not self.header.forward[self.level]:
                self.level -= 1
            return True
        return False
    
    def display(self):
        """打印跳跃表结构"""
        for i in range(self.level, -1, -1):
            print(f"Level {i}: ", end="")
            node = self.header.forward[i]
            while node:
                print(f"{node.value} --> ", end="")
                node = node.forward[i]
            print("None")

# 测试代码
if __name__ == "__main__":
    sl = SkipList()
    values = [3, 6, 7, 9, 12, 19, 17, 26, 21, 25]
    for val in values:
        sl.insert(val)
    
    print("跳跃表结构:")
    sl.display()
    
    print("\n查找值 19:", sl.search(19))
    print("查找值 15:", sl.search(15))
    
    print("\n删除值 19:", sl.delete(19))
    print("跳跃表结构:")
    sl.display()
```

#### 时间复杂度分析

- **查找**：平均时间复杂度O(logn)，最坏时间复杂度O(n)
- **插入**：平均时间复杂度O(logn)，最坏时间复杂度O(n)
- **删除**：平均时间复杂度O(logn)，最坏时间复杂度O(n)
- **空间复杂度**：O(n)，其中n是元素个数

#### 应用场景

- **Redis有序集合**：Redis使用跳跃表作为有序集合的底层实现之一
- **LevelDB**：Google开发的键值存储数据库使用跳跃表
- **SkipListMap**：一些编程语言的标准库实现
- **需要高效范围查询的场景**：如排行榜、区间查询等

跳跃表通过牺牲一定的空间复杂度，换取了查找、插入和删除操作的高效性，同时保持了实现的简洁性，是一种非常实用的数据结构。

### 10.4 红黑树 vs 跳跃表

| 特性 | 红黑树 | 跳跃表 |
|------|--------|--------|
| 查找复杂度 | O(logn) | O(logn) |
| 插入复杂度 | O(logn) | O(logn) |
| 删除复杂度 | O(logn) | O(logn) |
| 实现难度 | 高 | 低 |
| 空间复杂度 | O(n) | O(n) |
| 范围查询 | 支持 | 支持（更高效） |
| 并发操作 | 复杂 | 相对简单 |

## 11. 一致性算法

### 11.1 为什么需要一致性
1. 数据不能存在单个节点（主机）上，否则可能出现单点故障
2. 多个节点（主机）需要保证具有相同的数据
3. 一致性算法就是为了解决上面两个问题

### 11.2 一致性分类

#### 强一致性
- **说明**：保证系统改变提交以后立即改变集群的状态
- **模型**：
  - Paxos
  - Raft（multi-paxos）
  - ZAB（multi-paxos）

#### 弱一致性
- **说明**：也叫最终一致性，系统不保证改变提交以后立即改变集群的状态，但是随着时间的推移最终状态是一致的
- **模型**：
  - DNS系统
  - Gossip协议

### 11.2 一致性算法的基本原理

一致性算法的核心思想是通过消息传递和投票机制，在存在故障的情况下确保所有节点对某个值达成一致。其基本原理包括：

1. **选举机制**：在分布式系统中选举出一个领导者（Leader），由领导者负责协调一致性过程
2. **投票过程**：通过多轮投票，确保多数节点对某个值达成一致
3. **日志复制**：领导者将操作记录到日志中，并复制到其他节点
4. **状态机复制**：所有节点按照相同的顺序执行相同的操作，确保状态一致
5. **容错机制**：能够处理节点故障、网络分区等异常情况

### 11.3 一致性算法的基本过程

一致性算法的基本过程通常包括以下步骤：

1. **提议阶段**（Proposal Phase）：
   - 某个节点（提议者）提出一个值
   - 向其他节点发送提议消息

2. **投票阶段**（Voting Phase）：
   - 其他节点（接受者）接收到提议
   - 根据一定规则决定是否接受提议
   - 向提议者回复投票结果

3. **决策阶段**（Decision Phase）：
   - 提议者收集投票结果
   - 如果获得多数票，确认提议通过
   - 向所有节点广播决策结果

4. **学习阶段**（Learning Phase）：
   - 所有节点学习并执行已确认的决策
   - 更新本地状态，达成一致

### 11.4 一致性算法的挑战

1. **网络分区**：网络故障导致系统被分割成多个独立的子系统
2. **节点故障**：节点崩溃、重启等异常情况
3. **消息丢失**：网络传输中消息丢失或延迟
4. **消息重复**：网络重试导致消息重复送达
5. **性能瓶颈**：一致性过程需要多轮通信，可能成为性能瓶颈

### 11.5 主流一致性算法

#### 11.5.1 Paxos算法

**基本概念**：
Paxos算法是由Leslie Lamport提出的一种基于消息传递的一致性协议，是分布式系统中最经典的一致性算法之一。

**角色**：
- **Proposer**（提议者）：提出提案，包括提案编号和值
- **Acceptor**（接受者）：对提案进行投票，决定是否接受
- **Learner**（学习者）：学习被通过的提案，不参与投票过程

**基本原理**：
Paxos算法通过两阶段提交（准备阶段和接受阶段）来确保一致性：

1. **准备阶段**（Prepare Phase）：
   - Proposer选择一个提案编号n，向所有Acceptor发送Prepare(n)消息
   - Acceptor收到消息后，如果n大于之前收到的所有提案编号，则回复Promise(n)，并承诺不再接受编号小于n的提案

2. **接受阶段**（Accept Phase）：
   - Proposer收到多数Acceptor的Promise后，选择一个值v（如果之前有接受的提案，则选择最大编号的提案的值，否则选择自己的提案值）
   - 向所有Acceptor发送Accept(n, v)消息
   - Acceptor收到消息后，如果n不小于之前承诺的最小编号，则接受该提案并回复Accepted(n, v)

3. **学习阶段**：
   - Proposer收到多数Acceptor的Accepted消息后，确认提案通过
   - 将通过的提案广播给所有Learner

**流程图**：

```
┌─────────────┐     Prepare(n)    ┌─────────────┐
│  Proposer   │──────────────────>│  Acceptor   │
└─────────────┘                   └─────────────┘
        ^                              │
        │         Promise(n, [v])       │
        └───────────────────────────────┘
        │
        ▼
┌─────────────┐    Accept(n, v)    ┌─────────────┐
│  Proposer   │──────────────────>│  Acceptor   │
└─────────────┘                   └─────────────┘
        ^                              │
        │         Accepted(n, v)        │
        └───────────────────────────────┘
        │
        ▼
┌─────────────┐    广播结果        ┌─────────────┐
│  Proposer   │──────────────────>│  Learner    │
└─────────────┘                   └─────────────┘
```

**特点**：
- **容错性强**：能处理最多f个节点故障（需要2f+1个节点）
- **能处理网络分区**：在网络分区恢复后仍能保持一致性
- **理论完备**：是第一个被证明正确的一致性算法
- **复杂度高**：理解和实现较为复杂

**应用**：Google Chubby

#### 11.5.2 Raft算法

**基本概念**：
Raft算法是由Diego Ongaro和John Ousterhout提出的一种分布式一致性协议，是Paxos的简化版，旨在更容易理解和实现。

**角色**：
- **Leader**（领导者）：负责接收客户端请求，复制日志到其他节点，并协调一致性过程
- **Follower**（跟随者）：被动接收Leader的消息，参与投票
- **Candidate**（候选人）：当Leader故障时，Follower转变为Candidate，参与Leader选举

**基本原理**：
Raft算法将一致性过程分为三个主要阶段：

1. **Leader选举**：
   - 当Follower超时未收到Leader的心跳时，转变为Candidate并开始选举
   - Candidate向其他节点发送投票请求
   - 如果获得多数票，则成为新的Leader
   - Leader通过定期发送心跳维持其地位

2. **日志复制**：
   - Leader接收客户端请求，将操作记录到日志中
   - Leader将日志条目复制到所有Follower
   - Follower确认收到后，Leader提交日志并通知Follower
   - Follower收到通知后提交日志并应用到状态机

3. **安全性**：
   - 通过任期号（Term）和日志索引确保日志的一致性
   - 选举时，Candidate必须包含所有已提交的日志条目
   - 处理网络分区和节点恢复的情况

**流程图**：

```
┌─────────────┐     心跳/日志     ┌─────────────┐
│  Leader     │──────────────────>│  Follower   │
└─────────────┘                   └─────────────┘
        ^                              │
        │          确认消息            │
        └───────────────────────────────┘
        │
        ▼
┌─────────────┐     选举超时      ┌─────────────┐
│  Follower   │──────────────────>│ Candidate   │
└─────────────┘                   └─────────────┘
        ^                              │
        │         投票请求            │
        └───────────────────────────────┘
        │
        ▼
┌─────────────┐     获得多数票     ┌─────────────┐
│ Candidate   │──────────────────>│  Leader     │
└─────────────┘                   └─────────────┘
```

**特点**：
- **易于理解**：通过分解问题和状态机的方式，降低了理解难度
- **实现简单**：相比Paxos，代码量更少，逻辑更清晰
- **安全性高**：通过严格的日志复制和选举规则确保一致性
- **可扩展性好**：支持集群成员变更、日志压缩等功能

**应用**：etcd、Consul

#### 11.5.3 ZAB算法

**基本概念**：
ZAB（ZooKeeper Atomic Broadcast）是专为ZooKeeper设计的一致性协议，基于主备复制的思想。

**基本原理**：
ZAB算法分为两个主要阶段：

1. **崩溃恢复**（Crash Recovery）：
   - 当Leader崩溃或网络分区导致Leader失去联系时，系统进入崩溃恢复阶段
   - 选举新的Leader，确保新Leader包含所有已提交的事务
   - 同步所有Follower的日志，确保集群状态一致

2. **原子广播**（Atomic Broadcast）：
   - 正常运行时，Leader负责接收客户端请求
   - Leader将请求转换为事务提案，分配全局唯一的事务ID（zxid）
   - Leader通过原子广播协议将提案复制到所有Follower
   - 当多数Follower确认收到后，Leader提交事务并通知所有Follower
   - Follower收到通知后提交事务并应用到状态机

**流程图**：

```
┌─────────────┐     崩溃/网络分区  ┌─────────────┐
│  正常状态   │──────────────────>│ 崩溃恢复    │
└─────────────┘                   └─────────────┘
        ^                              │
        │          选举新Leader        │
        └───────────────────────────────┘
        │
        ▼
┌─────────────┐     恢复完成      ┌─────────────┐
│  原子广播   │<──────────────────│ 崩溃恢复    │
└─────────────┘                   └─────────────┘
        │                              │
        │         接收客户端请求       │
        ▼                              │
┌─────────────┐     广播提案       ┌─────────────┐
│  Leader     │──────────────────>│  Follower   │
└─────────────┘                   └─────────────┘
        ^                              │
        │          确认消息            │
        └───────────────────────────────┘
        │
        ▼
┌─────────────┐     提交事务       ┌─────────────┐
│  Leader     │──────────────────>│  Follower   │
└─────────────┘                   └─────────────┘
```

**特点**：
- **专为ZooKeeper设计**：针对ZooKeeper的场景进行了优化
- **崩溃恢复能力**：能快速从Leader崩溃中恢复
- **顺序一致性**：确保所有事务按照相同的顺序执行
- **高性能**：通过批量处理和流水线机制提高性能

**应用**：ZooKeeper

#### 11.5.4 Gossip协议

**基本概念**：
Gossip协议是一种基于流行病传播的一致性协议，通过节点之间的随机通信来传播信息，最终达到全局一致。

**基本原理**：
Gossip协议的基本思想是：

1. **随机通信**：每个节点随机选择其他节点进行通信
2. **信息传播**：节点将自己的状态信息发送给随机选择的节点
3. **状态更新**：接收到信息的节点更新自己的状态，并继续传播
4. **最终一致性**：经过足够的时间后，所有节点的状态会达成一致

**流程图**：

```
┌─────────────┐     随机选择      ┌─────────────┐
│  Node A     │──────────────────>│  Node B     │
└─────────────┘                   └─────────────┘
        ^                              │
        │         交换状态信息        │
        └───────────────────────────────┘
        │                              │
        ▼                              ▼
┌─────────────┐     随机选择      ┌─────────────┐
│  Node A     │──────────────────>│  Node C     │
└─────────────┘                   └─────────────┘
        ^                              │
        │         交换状态信息        │
        └───────────────────────────────┘
        │                              │
        ▼                              ▼
┌─────────────┐     随机选择      ┌─────────────┐
│  Node B     │──────────────────>│  Node D     │
└─────────────┘                   └─────────────┘
```

**特点**：
- **扩展性好**：节点数量增加时，通信开销呈线性增长
- **容错性强**：单个节点故障不影响整个系统
- **最终一致性**：不需要严格的多数派，最终所有节点会达成一致
- **去中心化**：没有固定的Leader，所有节点地位平等
- **适用于大规模系统**：特别适合节点数量众多的分布式系统

**应用**：Cassandra、Redis Cluster

### 11.6 主流一致性算法比较

| 算法 | 一致性类型 | 适用规模 | 容错能力 | 性能 | 实现复杂度 | 典型应用 |
|------|-----------|----------|----------|------|------------|----------|
| Paxos | 强一致性 | 中小规模 | 高 | 中 | 高 | 分布式数据库 |
| Raft | 强一致性 | 中小规模 | 高 | 中 | 中 | etcd、Consul |
| ZAB | 强一致性 | 中小规模 | 高 | 中 | 中 | ZooKeeper |
| Gossip | 最终一致性 | 大规模 | 高 | 高 | 低 | Cassandra、DynamoDB |

### 11.7 一致性算法的应用场景

1. **分布式数据库**：确保数据在多个节点之间的一致性，如Google Spanner、CockroachDB

2. **分布式协调服务**：如ZooKeeper、etcd，用于配置管理、服务发现等

3. **区块链技术**：如比特币的工作量证明（PoW）、以太坊的权益证明（PoS）

4. **分布式文件系统**：如HDFS，确保文件元数据的一致性

5. **微服务架构**：确保服务之间的状态一致性，如服务注册与发现

## 8. 面试题解析

### 1. 请解释时间复杂度和空间复杂度的概念

**答案**：
- **时间复杂度**：衡量算法执行时间随输入规模增长的变化趋势，通常用大O符号表示，如O(n)、O(logn)、O(n²)等。它表示的是算法运行时间的上界，描述了算法的渐近行为。
- **空间复杂度**：衡量算法所需存储空间随输入规模增长的变化趋势，同样用大O符号表示。它包括算法本身的存储空间、输入数据的存储空间以及算法运行过程中临时使用的存储空间。

### 2. 什么是稳定排序？哪些排序算法是稳定的？

**答案**：
- **稳定排序**：在排序过程中，相同值的元素在排序后相对位置保持不变的排序算法。
- **稳定的排序算法**：冒泡排序、插入排序、归并排序、计数排序、桶排序、基数排序。
- **不稳定的排序算法**：选择排序、快速排序、堆排序。

### 3. 请解释二分查找的原理和适用条件

**答案**：
- **原理**：在有序数组中，每次将搜索范围减半，通过比较中间元素与目标值的大小，确定目标值在左半部分还是右半部分，递归或迭代地进行搜索。
- **适用条件**：
  1. 数据结构必须是有序的
  2. 数据结构必须支持随机访问（如数组），不适合链表等不支持随机访问的结构

### 4. 什么是动态规划？请举例说明

**答案**：
- **动态规划**：通过将原问题分解为相对简单的子问题，先求解子问题，然后从这些子问题的解得到原问题的解。核心思想是记忆化搜索，避免重复计算。
- **示例**：斐波那契数列
  - 递归解法：时间复杂度O(2^n)，存在大量重复计算
  - 动态规划解法：使用数组存储中间结果，时间复杂度O(n)

### 5. 红黑树和AVL树的区别是什么？

**答案**：
- **平衡条件**：AVL树要求左右子树高度差不超过1，红黑树通过颜色约束（每个红色节点的两个子节点都是黑色，从任一节点到其每个叶子的所有路径都包含相同数目的黑色节点）来保证树的平衡。
- **插入删除操作**：AVL树在插入删除时可能需要更多的旋转操作来保持平衡，红黑树的旋转操作相对较少。
- **查询性能**：AVL树更平衡，查询性能略好；红黑树插入删除性能略好。
- **应用场景**：AVL树适用于查询频繁的场景，红黑树适用于插入删除频繁的场景。

### 6. 什么是跳跃表？它有什么优势？

**答案**：
- **跳跃表**：是一种有序数据结构，通过在每个节点中维护多个指向其他节点的指针，从而达到快速访问节点的目的。
- **优势**：
  1. 查找、插入、删除的时间复杂度均为O(logn)
  2. 实现简单，相比平衡树更容易理解和实现
  3. 范围查询效率高
  4. 并发操作相对容易实现

### 7. 请解释一致性算法的作用和分类

**答案**：
- **作用**：在分布式系统中，保证多个节点之间的数据一致性，避免单点故障，提高系统的可靠性和可用性。
- **分类**：
  1. **强一致性**：保证系统改变提交以后立即改变集群的状态，如Paxos、Raft、ZAB算法。
  2. **弱一致性**：也叫最终一致性，系统不保证改变提交以后立即改变集群的状态，但是随着时间的推移最终状态是一致的，如Gossip协议。

### 8. 请解释分治算法的基本思想和应用场景

**答案**：
- **基本思想**：将问题分解为多个子问题，分别解决后合并结果。
- **步骤**：
  1. 分解：将问题分解为子问题
  2. 解决：递归解决子问题
  3. 合并：将子问题的解合并为原问题的解
- **应用场景**：归并排序、快速排序、二分搜索、大数乘法、Strassen矩阵乘法等。

## 12. 算法思想总结与比较

### 12.1 算法思想概览

| 算法思想 | 基本概念 | 核心思路 | 适用场景 |
|---------|---------|---------|----------|
| 排序算法 | 将数据按照特定顺序排列 | 通过比较和交换元素，逐步构建有序序列 | 需要对数据进行排序的场景 |
| 搜索算法 | 在数据结构中查找特定元素 | 根据特定策略，在数据结构中定位目标元素 | 需要在数据结构中查找元素的场景 |
| 暴力算法 | 枚举所有可能的情况 | 生成所有可能的解决方案，验证每个方案 | 问题规模小，或者没有更好的算法时 |
| 递归算法 | 函数调用自身解决问题 | 将复杂问题分解为相似的子问题，递归求解 | 具有递归结构的问题，如树、图的遍历 |
| 分治算法 | 将问题分解为子问题 | 分解问题，递归求解子问题，合并结果 | 可分解为独立子问题的问题，如排序、搜索 |
| 动态规划 | 利用重叠子问题和最优子结构 | 定义状态，建立状态转移方程，自底向上求解 | 具有重叠子问题和最优子结构的问题 |
| 贪心算法 | 每一步都做出局部最优选择 | 按照贪心策略，逐步构建解决方案 | 具有贪心选择性质的问题 |
| 回溯算法 | 探索所有可能的候选解 | 选择选项，探索结果，回溯尝试其他选项 | 约束满足问题，如排列组合、N皇后 |
| 分支限界算法 | 系统地搜索解空间树 | 计算上下界，使用优先级队列选择节点，剪枝 | 组合优化问题，如旅行商问题、背包问题 |

### 12.2 时间复杂度比较

| 算法思想 | 平均时间复杂度 | 最坏时间复杂度 | 最好时间复杂度 |
|---------|---------------|---------------|---------------|
| 排序算法 | O(nlogn) (快速排序、归并排序) | O(n²) (冒泡排序、插入排序) | O(n) (冒泡排序、插入排序) |
| 搜索算法 | O(logn) (二分搜索) | O(n) (线性搜索) | O(1) (哈希表搜索) |
| 暴力算法 | O(2^n) 或 O(n!) | O(2^n) 或 O(n!) | O(n) |
| 递归算法 | 取决于具体问题 | 取决于具体问题 | 取决于具体问题 |
| 分治算法 | O(nlogn) (归并排序) | O(nlogn) (归并排序) | O(nlogn) (归并排序) |
| 动态规划 | O(n²) 或 O(n³) | O(n²) 或 O(n³) | O(n²) 或 O(n³) |
| 贪心算法 | O(n) 或 O(nlogn) | O(n) 或 O(nlogn) | O(n) 或 O(nlogn) |
| 回溯算法 | O(2^n) 或 O(n!) | O(2^n) 或 O(n!) | O(n) |
| 分支限界算法 | O(b^d) (b是分支因子，d是解的深度) | O(b^d) | O(d) |

### 12.3 空间复杂度比较

| 算法思想 | 空间复杂度 | 说明 |
|---------|-----------|------|
| 排序算法 | O(1) (原地排序) 或 O(n) (归并排序) | 取决于具体的排序算法 |
| 搜索算法 | O(1) (线性搜索、二分搜索) 或 O(V) (DFS、BFS) | 取决于具体的搜索算法 |
| 暴力算法 | O(n) | 需要存储所有可能的解决方案 |
| 递归算法 | O(d) (d是递归深度) | 递归调用栈的空间 |
| 分治算法 | O(n) (归并排序) | 需要存储子问题的解 |
| 动态规划 | O(n²) 或 O(n) (空间优化后) | 需要存储状态表 |
| 贪心算法 | O(1) | 只需要常数级别的额外空间 |
| 回溯算法 | O(d) (d是递归深度) | 递归调用栈的空间 |
| 分支限界算法 | O(b^d) | 需要存储解空间树的节点 |

### 12.4 算法选择指南

1. **根据问题规模选择**：
   - 小规模问题：暴力算法、回溯算法
   - 中等规模问题：贪心算法、分治算法
   - 大规模问题：动态规划、分治算法

2. **根据问题特性选择**：
   - 有重叠子问题和最优子结构：动态规划
   - 具有贪心选择性质：贪心算法
   - 可分解为独立子问题：分治算法
   - 约束满足问题：回溯算法
   - 组合优化问题：分支限界算法

3. **根据时间和空间限制选择**：
   - 时间限制严格：选择时间复杂度低的算法，如动态规划、贪心算法
   - 空间限制严格：选择空间复杂度低的算法，如原地排序、贪心算法

4. **根据实现复杂度选择**：
   - 实现简单：暴力算法、贪心算法
   - 实现中等：分治算法、动态规划
   - 实现复杂：回溯算法、分支限界算法

### 12.5 算法思想的结合使用

在实际应用中，往往需要结合多种算法思想来解决复杂问题：

1. **动态规划 + 贪心算法**：对于某些问题，先使用贪心算法得到近似解，再使用动态规划进行优化

2. **分治算法 + 动态规划**：将问题分解为子问题，对子问题使用动态规划求解

3. **回溯算法 + 剪枝**：在回溯过程中，结合贪心策略进行剪枝，提高效率

4. **分支限界算法 + 动态规划**：使用动态规划计算上下界，加速分支限界算法的搜索过程

5. **排序算法 + 搜索算法**：先对数据进行排序，然后使用二分搜索等高效搜索算法

### 12.6 总结

算法思想是解决问题的核心工具，不同的算法思想适用于不同类型的问题。选择合适的算法思想，不仅可以提高问题解决的效率，还可以降低代码的复杂度和维护成本。

在学习和应用算法思想时，应该：

1. 理解每种算法思想的基本概念和核心思路
2. 掌握每种算法思想的适用场景和优缺点
3. 学会分析问题，选择合适的算法思想
4. 能够结合多种算法思想解决复杂问题
5. 不断实践，积累经验，提高算法设计能力

## 13. 参考链接

### 排序算法
- [排序算法详解 - 力扣](https://leetcode-cn.com/problems/sort-an-array/solution/) 
- [十大经典排序算法 - 菜鸟教程](https://www.runoob.com/w3cnote/ten-sorting-algorithm.html)
- [排序算法时间复杂度、空间复杂度、稳定性比较](https://www.cnblogs.com/onepixel/p/7674659.html)

### 搜索算法
- [搜索算法 - 维基百科](https://zh.wikipedia.org/wiki/搜索算法)
- [深度优先搜索和广度优先搜索 - 力扣](https://leetcode-cn.com/tag/breadth-first-search/)

### 动态规划
- [动态规划入门 - 力扣](https://leetcode-cn.com/tag/dynamic-programming/)
- [动态规划详解 - 知乎](https://zhuanlan.zhihu.com/p/91582909)

### 数据结构
- [数据结构与算法 - 极客时间](https://time.geekbang.org/column/intro/100017301)
- [数据结构详解 - 维基百科](https://zh.wikipedia.org/wiki/数据结构)

### 跳跃表
- [跳跃表：为什么 Redis 使用它而不是平衡树？](https://zhuanlan.zhihu.com/p/637407262)
- [跳跃表原理解析](https://mp.weixin.qq.com/s?__biz=MzU0ODMyNDk0Mw==&mid=2247495510&idx=1&sn=7a9f174b2a5facd92ee0efccf712eecc&chksm=fb427c76cc35f560d0ce02d6b7ff2f3e28c0349434734a428b20dfa2c3366d6266b15eacb588&scene=27)
- [跳跃表论文](https://epaperpress.com/sortsearch/download/skiplist.pdf)

### 一致性算法
- [一致性算法详解 - 知乎](https://zhuanlan.zhihu.com/p/130332285)
- [Paxos算法详解](https://zhuanlan.zhihu.com/p/31780743)
- [Raft算法详解](https://zhuanlan.zhihu.com/p/32052223)
- [ZAB协议详解](https://zhuanlan.zhihu.com/p/37473762)

## 14. LeetCode题目示例

### 14.1 两数之和 (Two Sum)

**原题链接**：[LeetCode 1. Two Sum](https://leetcode.cn/problems/two-sum/)

**题目描述**：给定一个整数数组 `nums` 和一个目标值 `target`，请你在该数组中找出和为目标值的那两个整数，并返回它们的数组下标。

**解题思路**：使用哈希表存储已经遍历过的元素及其索引，对于每个元素，检查哈希表中是否存在 `target - nums[i]`，如果存在，则返回两个元素的索引。

**Java代码实现**：

```java
import java.util.HashMap;
import java.util.Map;

public class TwoSum {
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                return new int[]{map.get(complement), i};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    // 测试
    public static void main(String[] args) {
        int[] nums = {2, 7, 11, 15};
        int target = 9;
        int[] result = twoSum(nums, target);
        System.out.println("Indices: [" + result[0] + ", " + result[1] + "]");
        // 输出: Indices: [0, 1]
    }
}
```

### 14.2 爬楼梯 (Climbing Stairs)

**原题链接**：[LeetCode 70. Climbing Stairs](https://leetcode.cn/problems/climbing-stairs/)

**题目描述**：假设你正在爬楼梯。需要 `n` 阶你才能到达楼顶。每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶？

**解题思路**：使用动态规划，定义 `dp[i]` 表示爬到第 `i` 阶的不同方法数。状态转移方程为 `dp[i] = dp[i-1] + dp[i-2]`，因为最后一步可以爬 1 阶或 2 阶。

**Java代码实现**：

```java
public class ClimbingStairs {
    public static int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    // 测试
    public static void main(String[] args) {
        int n = 5;
        int result = climbStairs(n);
        System.out.println("不同的方法数: " + result);
        // 输出: 不同的方法数: 8
    }
}
```

### 14.3 子集 (Subsets)

**原题链接**：[LeetCode 78. Subsets](https://leetcode.cn/problems/subsets/)

**题目描述**：给定一组不含重复元素的整数数组 `nums`，返回该数组所有可能的子集（幂集）。

**解题思路**：使用回溯算法，从空集开始，逐步添加元素，生成所有可能的子集。

**Java代码实现**：

```java
import java.util.ArrayList;
import java.util.List;

public class Subsets {
    public static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        backtrack(result, new ArrayList<>(), nums, 0);
        return result;
    }

    private static void backtrack(List<List<Integer>> result, List<Integer> tempList, int[] nums, int start) {
        result.add(new ArrayList<>(tempList));
        for (int i = start; i < nums.length; i++) {
            tempList.add(nums[i]);
            backtrack(result, tempList, nums, i + 1);
            tempList.remove(tempList.size() - 1);
        }
    }

    // 测试
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = subsets(nums);
        System.out.println("所有子集:");
        for (List<Integer> subset : result) {
            System.out.println(subset);
        }
        // 输出:
        // []
        // [1]
        // [1, 2]
        // [1, 2, 3]
        // [1, 3]
        // [2]
        // [2, 3]
        // [3]
    }
}
```

## 15. 大厂经典算法题目汇总

本章节汇总了国内外大厂（字节跳动、阿里巴巴、腾讯、美团、百度、Google、Facebook、Amazon等）面试中高频出现的经典算法题目，涵盖数组、链表、树、动态规划、回溯等多个领域。

### 15.1 数组与字符串

#### 15.1.1 三数之和 (3Sum)

**原题链接**：[LeetCode 15. 3Sum](https://leetcode.cn/problems/3sum/)

**题目描述**：给你一个整数数组 `nums` ，判断是否存在三元组 `[nums[i], nums[j], nums[k]]` 满足 `i != j`、`i != k` 且 `j != k` ，同时还满足 `nums[i] + nums[j] + nums[k] == 0` 。请你返回所有和为 `0` 且不重复的三元组。

**解题思路**：
1. 首先对数组进行排序
2. 固定一个数，使用双指针在剩余部分寻找两个数，使三数之和为0
3. 注意去重：跳过相同的元素，避免重复的三元组

**时间复杂度**：O(n²)，排序O(nlogn)，双指针遍历O(n²)
**空间复杂度**：O(1)，不考虑结果存储空间

**Java代码实现**：

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return result;
        }
        
        // 排序
        Arrays.sort(nums);
        
        for (int i = 0; i < nums.length - 2; i++) {
            // 去重：跳过相同的元素
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }
            
            // 如果当前数字大于0，则三数之和一定大于0
            if (nums[i] > 0) {
                break;
            }
            
            int left = i + 1;
            int right = nums.length - 1;
            
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                
                if (sum == 0) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));
                    
                    // 去重
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }
                    
                    left++;
                    right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        
        return result;
    }

    // 测试
    public static void main(String[] args) {
        int[] nums = {-1, 0, 1, 2, -1, -4};
        List<List<Integer>> result = threeSum(nums);
        System.out.println("三数之和为0的所有组合:");
        for (List<Integer> list : result) {
            System.out.println(list);
        }
        // 输出: [-1, -1, 2], [-1, 0, 1]
    }
}
```

---

#### 15.1.2 合并区间 (Merge Intervals)

**原题链接**：[LeetCode 56. Merge Intervals](https://leetcode.cn/problems/merge-intervals/)

**题目描述**：以数组 `intervals` 表示若干个区间的集合，其中单个区间为 `intervals[i] = [starti, endi]` 。请你合并所有重叠的区间，并返回一个不重叠的区间数组，该数组需恰好覆盖输入中的所有区间。

**解题思路**：
1. 按区间的起始位置排序
2. 遍历区间，如果当前区间的起始位置小于等于上一个区间的结束位置，则合并
3. 否则，将上一个区间加入结果，更新当前区间

**时间复杂度**：O(nlogn)，主要是排序
**空间复杂度**：O(n)，存储结果

**Java代码实现**：

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {
    public static int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        
        // 按区间起始位置排序
        Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
        
        List<int[]> result = new ArrayList<>();
        int[] current = intervals[0];
        
        for (int i = 1; i < intervals.length; i++) {
            // 如果当前区间与下一个区间重叠
            if (current[1] >= intervals[i][0]) {
                // 合并区间，更新结束位置
                current[1] = Math.max(current[1], intervals[i][1]);
            } else {
                // 不重叠，加入结果
                result.add(current);
                current = intervals[i];
            }
        }
        
        // 加入最后一个区间
        result.add(current);
        
        return result.toArray(new int[result.size()][]);
    }

    // 测试
    public static void main(String[] args) {
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] result = merge(intervals);
        System.out.println("合并后的区间:");
        for (int[] interval : result) {
            System.out.println(Arrays.toString(interval));
        }
        // 输出: [1, 6], [8, 10], [15, 18]
    }
}
```

---

#### 15.1.3 最长无重复子串 (Longest Substring Without Repeating Characters)

**原题链接**：[LeetCode 3. Longest Substring Without Repeating Characters](https://leetcode.cn/problems/longest-substring-without-repeating-characters/)

**题目描述**：给定一个字符串 `s` ，请你找出其中不含有重复字符的最长子串的长度。

**解题思路**：
1. 使用滑动窗口，维护一个窗口 `[left, right]`
2. 使用HashSet存储窗口内的字符
3. 右指针向右移动，如果遇到重复字符，左指针向右移动直到无重复
4. 记录最大窗口大小

**时间复杂度**：O(n)，每个字符最多被访问两次
**空间复杂度**：O(min(m, n))，m为字符集大小

**Java代码实现**：

```java
import java.util.HashSet;
import java.util.Set;

public class LongestSubstring {
    public static int lengthOfLongestSubstring(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }
        
        Set<Character> set = new HashSet<>();
        int left = 0;
        int maxLength = 0;
        
        for (int right = 0; right < s.length(); right++) {
            // 如果字符已存在，移动左指针
            while (set.contains(s.charAt(right))) {
                set.remove(s.charAt(left));
                left++;
            }
            
            set.add(s.charAt(right));
            maxLength = Math.max(maxLength, right - left + 1);
        }
        
        return maxLength;
    }

    // 测试
    public static void main(String[] args) {
        String s = "abcabcbb";
        int result = lengthOfLongestSubstring(s);
        System.out.println("最长无重复子串长度: " + result);
        // 输出: 3 ("abc")
    }
}
```

---

#### 15.1.4 接雨水 (Trapping Rain Water)

**原题链接**：[LeetCode 42. Trapping Rain Water](https://leetcode.cn/problems/trapping-rain-water/)

**题目描述**：给定 `n` 个非负整数表示每个宽度为 `1` 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。

**解题思路**：
1. 使用双指针法，分别从左右两端向中间移动
2. 维护左右两个最大值 `leftMax` 和 `rightMax`
3. 当 `height[left] < height[right]` 时，左指针右移，否则右指针左移
4. 计算当前位置能接的雨水量：`min(leftMax, rightMax) - height[i]`

**时间复杂度**：O(n)
**空间复杂度**：O(1)

**Java代码实现**：

```java
public class TrappingRainWater {
    public static int trap(int[] height) {
        if (height == null || height.length <= 2) {
            return 0;
        }
        
        int left = 0;
        int right = height.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int water = 0;
        
        while (left < right) {
            if (height[left] < height[right]) {
                // 左指针移动
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    water += leftMax - height[left];
                }
                left++;
            } else {
                // 右指针移动
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    water += rightMax - height[right];
                }
                right--;
            }
        }
        
        return water;
    }

    // 测试
    public static void main(String[] args) {
        int[] height = {0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1};
        int result = trap(height);
        System.out.println("能接的雨水量: " + result);
        // 输出: 6
    }
}
```

---

#### 15.1.5 盛最多水的容器 (Container With Most Water)

**原题链接**：[LeetCode 11. Container With Most Water](https://leetcode.cn/problems/container-with-most-water/)

**题目描述**：给定一个长度为 `n` 的整数数组 `height` 。有 `n` 条垂线，第 `i` 条线的两个端点是 `(i, 0)` 和 `(i, height[i])` 。找出其中的两条线，使得它们与 `x` 轴共同构成的容器可以容纳最多的水。

**解题思路**：
1. 使用双指针，分别从左右两端向中间移动
2. 计算当前容器的面积：`min(height[left], height[right]) * (right - left)`
3. 移动较短的那条边，寻找可能的更大面积

**时间复杂度**：O(n)
**空间复杂度**：O(1)

**Java代码实现**：

```java
public class ContainerWithMostWater {
    public static int maxArea(int[] height) {
        if (height == null || height.length < 2) {
            return 0;
        }
        
        int left = 0;
        int right = height.length - 1;
        int maxArea = 0;
        
        while (left < right) {
            int currentArea = Math.min(height[left], height[right]) * (right - left);
            maxArea = Math.max(maxArea, currentArea);
            
            // 移动较短的边
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }
        
        return maxArea;
    }

    // 测试
    public static void main(String[] args) {
        int[] height = {1, 8, 6, 2, 5, 4, 8, 3, 7};
        int result = maxArea(height);
        System.out.println("最大容器面积: " + result);
        // 输出: 49
    }
}
```

### 15.2 链表

#### 15.2.1 反转链表 (Reverse Linked List)

**原题链接**：[LeetCode 206. Reverse Linked List](https://leetcode.cn/problems/reverse-linked-list/)

**题目描述**：给你单链表的头节点 `head` ，请你反转链表，并返回反转后的链表。

**解题思路**：
1. 使用三个指针：prev、current、next
2. 遍历链表，将当前节点的next指向prev
3. 移动指针，继续处理下一个节点

**时间复杂度**：O(n)
**空间复杂度**：O(1)

**Java代码实现**：

```java
public class ReverseLinkedList {
    // 链表节点定义
    static class ListNode {
        int val;
        ListNode next;
        
        ListNode(int val) {
            this.val = val;
        }
    }
    
    public static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        return prev;
    }

    // 测试
    public static void main(String[] args) {
        // 构建链表: 1 -> 2 -> 3 -> 4 -> 5
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        
        ListNode reversed = reverseList(head);
        System.out.print("反转后的链表: ");
        while (reversed != null) {
            System.out.print(reversed.val + " ");
            reversed = reversed.next;
        }
        // 输出: 5 4 3 2 1
    }
}
```

---

#### 15.2.2 环形链表 II (Linked List Cycle II)

**原题链接**：[LeetCode 142. Linked List Cycle II](https://leetcode.cn/problems/linked-list-cycle-ii/)

**题目描述**：给定一个链表的头节点 `head` ，返回链表开始入环的第一个节点。如果链表无环，则返回 `null`。

**解题思路**：
1. 使用快慢指针判断是否有环
2. 如果有环，快指针回到头节点，慢指针保持在相遇点
3. 两个指针以相同速度移动，再次相遇点即为环的入口

**时间复杂度**：O(n)
**空间复杂度**：O(1)

**Java代码实现**：

```java
public class LinkedListCycleII {
    static class ListNode {
        int val;
        ListNode next;
        
        ListNode(int val) {
            this.val = val;
        }
    }
    
    public static ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }
        
        ListNode slow = head;
        ListNode fast = head;
        
        // 判断是否有环
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            if (slow == fast) {
                // 有环，找入口
                fast = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        
        return null;
    }
}
```

### 15.3 树与图

#### 15.3.1 二叉树的最近公共祖先 (Lowest Common Ancestor of a Binary Tree)

**原题链接**：[LeetCode 236. Lowest Common Ancestor of a Binary Tree](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/)

**题目描述**：给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。

**解题思路**：
1. 递归遍历树
2. 如果当前节点等于p或q，返回当前节点
3. 递归在左右子树中查找
4. 如果左右子树都找到，当前节点就是LCA
5. 如果只有一边找到，返回那一边的结果

**时间复杂度**：O(n)
**空间复杂度**：O(h)，h为树的高度

**Java代码实现**：

```java
public class LowestCommonAncestor {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int val) {
            this.val = val;
        }
    }
    
    public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }
        
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        // 如果左右子树都找到，当前节点就是LCA
        if (left != null && right != null) {
            return root;
        }
        
        // 返回非空的那一边
        return left != null ? left : right;
    }
}
```

---

#### 15.3.2 课程表 (Course Schedule)

**原题链接**：[LeetCode 207. Course Schedule](https://leetcode.cn/problems/course-schedule/)

**题目描述**：你这个学期必须选修 `numCourses` 门课程，记为 `0` 到 `numCourses - 1` 。在选修某些课程之前需要一些先修课程。给定课程总数和先修课程关系，判断是否可能完成所有课程的学习。

**解题思路**：
1. 构建有向图，表示课程依赖关系
2. 使用拓扑排序（Kahn算法或DFS）检测是否有环
3. 如果有环，则无法完成所有课程

**时间复杂度**：O(V + E)，V为课程数，E为先修关系数
**空间复杂度**：O(V + E)

**Java代码实现**：

```java
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CourseSchedule {
    public static boolean canFinish(int numCourses, int[][] prerequisites) {
        // 构建图和入度数组
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[numCourses];
        
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] prereq : prerequisites) {
            graph.get(prereq[1]).add(prereq[0]);
            inDegree[prereq[0]]++;
        }
        
        // Kahn算法
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int count = 0;
        while (!queue.isEmpty()) {
            int course = queue.poll();
            count++;
            
            for (int next : graph.get(course)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return count == numCourses;
    }

    // 测试
    public static void main(String[] args) {
        int numCourses = 2;
        int[][] prerequisites = {{1, 0}};
        boolean result = canFinish(numCourses, prerequisites);
        System.out.println("是否可以完成所有课程: " + result);
        // 输出: true
    }
}
```

### 15.4 动态规划

#### 15.4.1 最长公共子序列 (Longest Common Subsequence)

**原题链接**：[LeetCode 1143. Longest Common Subsequence](https://leetcode.cn/problems/longest-common-subsequence/)

**题目描述**：给定两个字符串 `text1` 和 `text2`，返回这两个字符串的最长公共子序列的长度。

**解题思路**：
1. 定义dp[i][j]为text1[0..i-1]和text2[0..j-1]的最长公共子序列长度
2. 如果text1[i-1] == text2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
3. 否则，dp[i][j] = max(dp[i-1][j], dp[i][j-1])

**时间复杂度**：O(m * n)
**空间复杂度**：O(m * n)，可优化至O(min(m, n))

**Java代码实现**：

```java
public class LongestCommonSubsequence {
    public static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }

    // 测试
    public static void main(String[] args) {
        String text1 = "abcde";
        String text2 = "ace";
        int result = longestCommonSubsequence(text1, text2);
        System.out.println("最长公共子序列长度: " + result);
        // 输出: 3
    }
}
```

---

#### 15.4.2 编辑距离 (Edit Distance)

**原题链接**：[LeetCode 72. Edit Distance](https://leetcode.cn/problems/edit-distance/)

**题目描述**：给你两个单词 `word1` 和 `word2`，请返回将 `word1` 转换成 `word2` 所使用的最少操作数。你可以对一个单词进行插入、删除、替换操作。

**解题思路**：
1. 定义dp[i][j]为word1[0..i-1]转换为word2[0..j-1]的最小操作数
2. 如果word1[i-1] == word2[j-1]，dp[i][j] = dp[i-1][j-1]
3. 否则，dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
   - dp[i-1][j] + 1：删除
   - dp[i][j-1] + 1：插入
   - dp[i-1][j-1] + 1：替换

**时间复杂度**：O(m * n)
**空间复杂度**：O(m * n)

**Java代码实现**：

```java
public class EditDistance {
    public static int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        
        // 初始化边界
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]) + 1;
                }
            }
        }
        
        return dp[m][n];
    }

    // 测试
    public static void main(String[] args) {
        String word1 = "horse";
        String word2 = "ros";
        int result = minDistance(word1, word2);
        System.out.println("最小操作数: " + result);
        // 输出: 3
    }
}
```

### 15.5 回溯算法

#### 15.5.1 全排列 (Permutations)

**原题链接**：[LeetCode 46. Permutations](https://leetcode.cn/problems/permutations/)

**题目描述**：给定一个不含重复数字的数组 `nums` ，返回其所有可能的全排列。

**解题思路**：
1. 使用回溯算法
2. 维护一个used数组标记已使用的元素
3. 递归构建排列，当路径长度等于数组长度时，加入结果

**时间复杂度**：O(n * n!)
**空间复杂度**：O(n)

**Java代码实现**：

```java
import java.util.ArrayList;
import java.util.List;

public class Permutations {
    public static List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        boolean[] used = new boolean[nums.length];
        backtrack(result, new ArrayList<>(), nums, used);
        return result;
    }
    
    private static void backtrack(List<List<Integer>> result, List<Integer> path, int[] nums, boolean[] used) {
        if (path.size() == nums.length) {
            result.add(new ArrayList<>(path));
            return;
        }
        
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            
            used[i] = true;
            path.add(nums[i]);
            backtrack(result, path, nums, used);
            path.remove(path.size() - 1);
            used[i] = false;
        }
    }

    // 测试
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        List<List<Integer>> result = permute(nums);
        System.out.println("所有全排列:");
        for (List<Integer> perm : result) {
            System.out.println(perm);
        }
        // 输出: [1,2,3], [1,3,2], [2,1,3], [2,3,1], [3,1,2], [3,2,1]
    }
}
```

---

#### 15.5.2 N皇后 (N-Queens)

**原题链接**：[LeetCode 51. N-Queens](https://leetcode.cn/problems/n-queens/)

**题目描述**：按照国际象棋的规则，皇后可以攻击与之处在同一行或同一列或同一斜线上的棋子。n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。

**解题思路**：
1. 使用回溯算法，逐行放置皇后
2. 检查当前位置是否安全（列、对角线）
3. 使用三个集合记录已占用的列和两个对角线

**时间复杂度**：O(n!)
**空间复杂度**：O(n)

**Java代码实现**：

```java
import java.util.ArrayList;
import java.util.List;

public class NQueens {
    public static List<List<String>> solveNQueens(int n) {
        List<List<String>> result = new ArrayList<>();
        char[][] board = new char[n][n];
        
        // 初始化棋盘
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] = '.';
            }
        }
        
        backtrack(result, board, 0, n);
        return result;
    }
    
    private static void backtrack(List<List<String>> result, char[][] board, int row, int n) {
        if (row == n) {
            result.add(construct(board));
            return;
        }
        
        for (int col = 0; col < n; col++) {
            if (isValid(board, row, col, n)) {
                board[row][col] = 'Q';
                backtrack(result, board, row + 1, n);
                board[row][col] = '.';
            }
        }
    }
    
    private static boolean isValid(char[][] board, int row, int col, int n) {
        // 检查列
        for (int i = 0; i < row; i++) {
            if (board[i][col] == 'Q') {
                return false;
            }
        }
        
        // 检查左上对角线
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
            if (board[i][j] == 'Q') {
                return false;
            }
        }
        
        // 检查右上对角线
        for (int i = row - 1, j = col + 1; i >= 0 && j < n; i--, j++) {
            if (board[i][j] == 'Q') {
                return false;
            }
        }
        
        return true;
    }
    
    private static List<String> construct(char[][] board) {
        List<String> result = new ArrayList<>();
        for (char[] row : board) {
            result.add(new String(row));
        }
        return result;
    }

    // 测试
    public static void main(String[] args) {
        int n = 4;
        List<List<String>> result = solveNQueens(n);
        System.out.println(n + "皇后问题的所有解法:");
        for (List<String> solution : result) {
            for (String row : solution) {
                System.out.println(row);
            }
            System.out.println();
        }
    }
}
```

### 15.6 大厂面试题汇总表

| 题目 | 难度 | 算法类型 | 出现频率 | 大厂 |
|------|------|----------|----------|------|
| 三数之和 | 中等 | 双指针、排序 | ⭐⭐⭐⭐⭐ | 字节、阿里、腾讯 |
| 合并区间 | 中等 | 排序、贪心 | ⭐⭐⭐⭐⭐ | 字节、美团、百度 |
| 最长无重复子串 | 中等 | 滑动窗口 | ⭐⭐⭐⭐⭐ | 所有大厂 |
| 反转链表 | 简单 | 链表操作 | ⭐⭐⭐⭐⭐ | 所有大厂 |
| 环形链表 II | 中等 | 双指针 | ⭐⭐⭐⭐ | 字节、阿里 |
| 二叉树最近公共祖先 | 中等 | 树、递归 | ⭐⭐⭐⭐⭐ | 所有大厂 |
| 课程表 | 中等 | 图、拓扑排序 | ⭐⭐⭐⭐ | 字节、腾讯 |
| 最长公共子序列 | 中等 | 动态规划 | ⭐⭐⭐⭐ | 阿里、腾讯 |
| 编辑距离 | 困难 | 动态规划 | ⭐⭐⭐⭐ | 字节、Google |
| 全排列 | 中等 | 回溯 | ⭐⭐⭐⭐⭐ | 所有大厂 |
| N皇后 | 困难 | 回溯 | ⭐⭐⭐ | 字节、Google |

### 15.7 解题技巧总结

1. **数组/字符串问题**：
   - 优先考虑双指针、滑动窗口
   - 需要排序时考虑快速排序或归并排序
   - 查找问题优先考虑哈希表

2. **链表问题**：
   - 熟练运用快慢指针
   - 注意处理边界情况（空链表、单节点）
   - 考虑使用哑节点简化操作

3. **树与图问题**：
   - 遍历：DFS（递归/栈）、BFS（队列）
   - 路径问题：回溯
   - 依赖关系：拓扑排序

4. **动态规划问题**：
   - 明确定义状态
   - 找出状态转移方程
   - 考虑空间优化（滚动数组）

5. **回溯问题**：
   - 画出递归树帮助理解
   - 注意剪枝优化
   - 去重技巧（排序、used数组）