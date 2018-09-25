package com.bage.study.algorithm.niuke;
/**
 * 给定一个double类型的浮点数base和int类型的整数exponent。求base的exponent次方。
 * 
 * https://www.nowcoder.com/practice/1a834e5e3e1a4b7ba251417554e07c00?tpId=13&tqId=11165&tPage=1&rp=1&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking
 * 
 * @author bage
 *
 */
public class PowerInt {
	
	 public double Power(double base, int exponent) {	
		 return  Math.pow(base, exponent);
     }


	 public static void main(String[] args) {

		double i = new PowerInt().Power(2.3,5);
		System.out.println(i);	

	 }
}