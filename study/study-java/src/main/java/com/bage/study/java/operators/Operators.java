package com.bage.study.java.operators;

/**
 * Java运算符
 * @author bage
 *
 */
public class Operators {

	
	public static void main(String[] args) {
		
		// 位运算符
		// Java语言中的位运算符主要有4种：&（位与）、|（位或）、^（异或）和~（按位取反）
		
		// 按位与
		byte a = 1,b = 2;
		System.out.println("二进制参数：" + String.format("%1$07d", Integer.parseInt(Integer.toBinaryString(b)))); // 
		System.out.println("二进制参数：" + Integer.toBinaryString(b)); // 

		System.out.println("二进制位与：" + Integer.toBinaryString(a&b)); // 都是1，返回1
		System.out.println("二进制位或：" + Integer.toBinaryString(a|b)); // 有1则1
		System.out.println("二进制异或：" + Integer.toBinaryString(a^b)); // 成双成对
		System.out.println("二进制取反：" + Integer.toBinaryString(~b)); // 
		System.out.println("二进制取反位数：" + Integer.toBinaryString(~b).length()); // 

		
		
	}
	
}
