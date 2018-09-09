package com.bage.study.algorithm.niuke;

import java.util.Scanner;

/**
 * 计算字符串最后一个单词的长度，单词以空格隔开。
 * 
 * https://www.nowcoder.com/practice/8c949ea5f36f422594b306a2300315da?tpId=37&tqId=21224&tPage=1&rp=&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 * @author bage
 *
 */
public class HWLastLength {

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);
		String str = scanner.nextLine();
		int index = str.lastIndexOf(" ");
		System.out.println(str.length() - index - 1);
		
		scanner.close();
	}
	
}
