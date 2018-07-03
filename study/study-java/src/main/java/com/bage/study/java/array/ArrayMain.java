package com.bage.study.java.array;

import java.util.Arrays;

/**
 * Arrays的基本使用
 * @author bage
 *
 */
public class ArrayMain {

	public static void main(String[] args) {
		Integer a[] = new Integer[]{2,5,7,1,9,2,};
		Arrays.sort(a);
		Integer b[] = new Integer[]{2,5,7,1,9,2,};
		Arrays.sort(b);
		System.out.println(Arrays.equals(a, b));// 顺序要一致
		System.out.println(Arrays.deepEquals(a,b)); 
		System.out.println(Arrays.toString(b));
		Arrays.fill(a,4); // 自动填充
		Integer[] bb = Arrays.copyOf(a, 10); // 拷贝
		Integer[] bbb = Arrays.copyOf(a, 5); // 拷贝
		System.out.println(Arrays.toString(bb));
		System.out.println(Arrays.toString(bbb));

	}
	
}
