package com.bage.study.algorithm.demos.niuke;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 明明想在学校中请一些同学一起做一项问卷调查，为了实验的客观性，他先用计算机生成了N个1到1000之间的随机整数（N≤1000），对于其中重复的数字，只保留一个，把其余相同的数去掉，不同的数对应着不同的学生的学号。然后再把这些数从小到大排序，按照排好的顺序去找同学做调查。请你协助明明完成“去重”与“排序”的工作(同一个测试用例里可能会有多组数据，希望大家能正确处理)。
 * 
 * https://www.nowcoder.com/practice/3245215fffb84b7b81285493eae92ff0?tpId=37&tqId=21226&rp=0&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking&tPage=1
 *
 */
public class HWRepeatSort 
{
    public static void main( String[] args )
    {
    	Scanner scanner = new Scanner(System.in);
		try {
			while (true) {
				int n = scanner.nextInt();
				Set<Integer> set = new HashSet<Integer>(n);
				for (int i = 0; i < n; i++) {
					set.add(scanner.nextInt());
				}
				
				Integer[] arrays = set.toArray(new Integer[0]);
				Arrays.sort(arrays);
				
				for (int i = 0; i < arrays.length; i++) {
					System.out.println(arrays[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

    }
}
