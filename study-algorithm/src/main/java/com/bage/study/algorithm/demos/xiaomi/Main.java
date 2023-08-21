package com.bage.study.algorithm.demos.xiaomi;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	static int r = 0;

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		int n = s.nextInt();
		int m = s.nextInt();
		int[] a = new int[n];
		judge(a, 1, m);
		System.out.println(r);

	}

	private static void judge(int[] a, int i, int m) {
		if (i == a.length - 1) {
			if (isOk(a, m))
				r++;
			return;
		}
		a[i] = -1;
		judge(a, i + 1, m);
		a[i] = 0;
		judge(a, i + 1, m);
		a[i] = 1;
		judge(a, i + 1, m);
		
	}

	private static boolean isOk(int[] a, int m) {
		int n = a.length;
		int t = 1;
		int temp = 0;
		ArrayList<Integer> in = new ArrayList<Integer>();
		for (int i = 1; i < n; i++) {
			switch (a[i]) {
			case -1:
				t += temp;
				temp = 0-(i + 1);
				break;
			case 0:
				temp = temp * 10 + i + 1;
				
				break;
			case 1:
				t += temp;
				temp = i + 1;
				break;

			default:
				break;
			}
		}
		if(t == m)
			return true;
		return false;
	}

}
