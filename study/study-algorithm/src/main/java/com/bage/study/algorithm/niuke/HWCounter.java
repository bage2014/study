package com.bage.study.algorithm.niuke;

import java.util.Scanner;

/**
 * 写出一个程序，接受一个由字母和数字组成的字符串，和一个字符，然后输出输入字符串中含有该字符的个数。不区分大小写。
 * 
 * https://www.nowcoder.com/practice/a35ce98431874e3a820dbe4b2d0508b1?tpId=37&tqId=21225&rp=0&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 * @author bage
 *
 */
public class HWCounter {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String str = scanner.next().toLowerCase();
		String chs = scanner.next().toLowerCase();
		
		int count = 0;
		for (int i = 0; i < str.length(); i++) {
			if(String.valueOf(str.charAt(i)).equals(chs)){
				count ++ ;
			}
		}
		System.out.println(count);
		scanner.close();
	}
	
}
