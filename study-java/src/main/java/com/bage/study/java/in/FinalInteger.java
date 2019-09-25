package com.bage.study.java.in;

public class FinalInteger {

	public static void main(String[] args) {
		
		Integer n = 10;
		n = 200;
		System.out.println(n);
		work(n);
		System.out.println(n);

		String.valueOf(123);
	}

	private static void work(Integer n) {
		n ++;		
	}
}
