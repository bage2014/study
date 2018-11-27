package com.bage.study.java.string;

/**
 * StringBuilder和StringBuffer
 * @author bage
 *
 */
public class BuilderBuffer {

	
	public static void main(String[] args) {
		
		String s = new String("dsds");
		System.out.println(s.hashCode());
		s = s + "dsdsd";
		System.out.println(s.hashCode());

		System.out.println("--------测试时间：-------------");
		int n = 100000;
		/**
		 * n = 100000;
		 *  add:3567
			addEquals:3225
			appendSbd:4
			appendSbf:5
		 */
		add(n);
		addEquals(n);
		appendSbd(n);
		appendSbf(n);
	}

	private static String appendSbf(int n) {
		long bf = System.currentTimeMillis();
		StringBuffer str = new StringBuffer("a");
		for (int i = 0; i < n; i++) {
			str.append("a");
		}
		System.out.println("appendSbf:" + (System.currentTimeMillis() - bf));
		return str.toString();
	}

	private static String appendSbd(int n) {
		long bf = System.currentTimeMillis();
		StringBuilder str = new StringBuilder("a");
		for (int i = 0; i < n; i++) {
			str.append("a");
		}
		System.out.println("appendSbd:" + (System.currentTimeMillis() - bf));
		return str.toString();
	}

	private static String addEquals(int n) {
		long bf = System.currentTimeMillis();
		String str = "a";
		for (int i = 0; i < n; i++) {
			str += "a";
		}
		System.out.println("addEquals:" + (System.currentTimeMillis() - bf));
		return str;
	}

	private static String add(int n) {
		long bf = System.currentTimeMillis();
		String str = "a";
		for (int i = 0; i < n; i++) {
			str = str + "a";
		}
		System.out.println("add:" + (System.currentTimeMillis() - bf));
		return str;
	}
	
}
