package com.bage.study.java.in;

public class Finally {

	public static void main(String[] args) {
		System.out.println(work()); // -1
		
	}

	@SuppressWarnings("finally")
	private static int work() {
		try {
			return 1;
		} finally {
			return -1;
		}
	}
}
