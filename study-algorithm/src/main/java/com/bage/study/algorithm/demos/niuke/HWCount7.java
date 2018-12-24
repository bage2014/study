package com.bage.study.algorithm.demos.niuke;

import java.util.Scanner;

/**
 * 计算7进制数和
 * 
 * @author bage
 *
 */
public class HWCount7 {
	public static void main(String[] args) {
		// 输入
		Scanner sc = null;
		try {
			sc = new Scanner(System.in);
			String str1 = sc.next();
			String str2 = sc.next();

			int max = str1.length() > str2.length() ? str1.length() : str2.length();

			// 初始化
			int a[] = new int[max];
			int b[] = new int[max];
			for (int i = 0; i < str1.length(); i++) {
				a[(max - str1.length()) + i] = Integer.parseInt(String.valueOf(str1.charAt(i)));
			}
			for (int i = 0; i < str2.length(); i++) {
				b[(max - str2.length()) + i] = Integer.parseInt(String.valueOf(str2.charAt(i)));
			}

			// 补 0
//			if (str1.length() > str2.length()) {
//				for (int i = 0; i < max - str2.length(); i++) {
//					b[i] = 0;
//				}
//			} else {
//				for (int i = 0; i < max - str1.length(); i++) {
//					a[i] = 0;
//				}
//			}

			// 求和
			int N = 7; // 7进制数
			int append = 0; // 进 1 ？
			int sum = 0;
			for (int i = max - 1; i >= 0; i--) {
				sum = append + a[i] + b[i];
				a[i] = sum % N;
				append = sum >= N ? 1 : 0;
			}

			// 合并结果
			StringBuilder sb = new StringBuilder();
			if (append == 1) {
				sb.append(append);
			}
			for (int i = 0; i < max; i++) {
				sb.append(a[i]);
			}
			// 输出
			System.out.println(sb.toString());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(sc != null){
				sc.close();
			}
		}
	}
}
