# study-algorithm #
算法学习笔记

## 参考链接

图解算法
https://github.com/krahets/hello-algo  、  https://www.hello-algo.com/

东哥
https://github.com/labuladong/fucking-algorithm

## 刷题地址 ##
- lintcode：[https://www.lintcode.com/problem/](https://www.lintcode.com/problem/ "https://www.lintcode.com/problem/")
- 牛客网： [https://www.nowcoder.com/ta/coding-interviews?page=1](https://www.nowcoder.com/ta/coding-interviews?page=1 "https://www.nowcoder.com/ta/coding-interviews?page=1")

## 算法特性 ##
- 确定性:每一种运算必须要有确切的定义，无二义性
- 能行性:运算都是基本运算，原理上能在有限时间内完成
- 输入:有0个或多个输入
- 输出:一个或多个输出
- 有穷性:在执行了有穷步运算后终止

## 算法求解思路 ##
- 理解问题
- 决策：
 - 计算设备能力 
 - 使用精确解或者近似解
 - 数据结构 
 - 算法设计策略
- 设计算法
- 验证算法正确性
- 算法分析
- 编码实现

## 排序问题 ##
- 查找问题
- 串处理问题
- 图论问题
- 组合问题
- 计算几何问题
- 数值计算问题
- 具体工程中各类有代表性的问题和算法

## 算法设计策略 ##
- Brute force 暴力法
 - 一种简单直接地解决问题的方法，常常直接基于问题本身的定义或所涉及到的概念
 - 我们可以用蛮力法来解决范围广泛的各领域的实际问题（实际上它可能是唯一一种能解决所有问题的算法策略）
 - 对于一些重的问题来说（排序、查找、矩阵乘法、字符串匹配等），蛮力策略可以产生有实用价值的算法
 - 如果要解决的问题的规模不是很大，蛮力法可以用一种能够接受的速度来地实例求解
- Greedy approach 贪心法
- Divide and conquer 分治法
- Dynamic programming 动态规划
- Decrease and conquer 
- Backtracking and Branch and bound
- Transform and conquer
- Space and time tradeoffs

## 理论分析 ##
- 明确输入数据规模n(数据量的大小)
- 确定时间单位(基本操作的执行次数)
- 分析最坏情况时间与数据规模的关系T=T(n)
 - 非递归算法 一般是多项式
 - 递归算法一般是递推关系
 - 如果需要，分析最好情况和平均情况下时间复杂度 T=T(n)
- 渐进效率分析（O，Θ，Ω表示）
- 增长速度分析

## demo ##
- 求最大公约数
 - 实现 com.bage.study.algorithm.demos.ppts.Gcd
- 排序 [https://www.lintcode.com/problem/sort-integers/description](https://www.lintcode.com/problem/sort-integers/description "https://www.lintcode.com/problem/sort-integers/description")
 - 实现(插入排序)：com.bage.study.algorithm.sort.InsertSort