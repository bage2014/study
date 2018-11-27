package com.bage.study.java.random;

import java.util.Random;

public class RandomUtils {

	public static void main(String[] args) {
		
		int number = 30;
		int min = 0;
		int max = 30;
		
		int randoms[] = getRandoms(number,min,max);
		
		for (int i = 0; i < randoms.length; i++) {
			if(i % 10 == 0) {
				System.out.println("");
			}
			System.out.print(randoms[i] + "\t");
		}
		
		System.out.println("\n----------------------------------");
		randoms = getRandoms(5,new int[] {1,2,3,4,5,6,7,8,9});
		
		for (int i = 0; i < randoms.length; i++) {
			if(i % 10 == 0) {
				System.out.println();
			}
			System.out.print(randoms[i] + "\t");
		}
	}

	/**
	 * 
	 * @param number 随机数个数
	 * @param min 最小值[包含]
	 * @param max 最大值[包含]
	 * @return
	 */
	private static int[] getRandoms(int number, int min, int max) {
		int n = max - min + 1;
		int a[] = new int[n];
		for (int i = 0; i < a.length; i++) {
			a[i] = min + i ;
		}
		
		Random  random = new Random();
		int res[] = new int[number];
		for (int i = 0; i < number; i++) {
			int tempIndex = random.nextInt(n);
			
			int temp = a[a.length - 1 - i];
			a[a.length - 1 - i] = a[tempIndex];
			a[tempIndex] = temp;
			
			res[i] = a[a.length - 1 - i];
			n -- ;
		}
		
		return res;
	}
	/**
	 * 
	 * @param number 个数
	 * @param a 在a【】里面随机选择 number个
	 * @return
	 */
	private static int[] getRandoms(int number,int[] a) {
		int n = a.length;
		Random  random = new Random();
		int res[] = new int[number];
		for (int i = 0; i < number; i++) {
			int tempIndex = random.nextInt(n);
			
			int temp = a[a.length - 1 - i];
			a[a.length - 1 - i] = a[tempIndex];
			a[tempIndex] = temp;
			
			res[i] = a[a.length - 1 - i];
			n -- ;
		}
		
		return res;
	}
	
}
