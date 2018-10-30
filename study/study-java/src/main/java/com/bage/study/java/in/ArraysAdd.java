package com.bage.study.java.in;

import java.util.Arrays;
import java.util.List;

public class ArraysAdd {

	public static void main(String[] args) {
		
		String[] strs = {"a","b","c"};
		List<String> list = Arrays.asList(strs);
		for (int i = 0; i < strs.length; i++) {
			System.out.println(list.get(i)); // 支持
		}
		System.out.println(list.size()); // 支持
		list.set(0, "cc");
		for (int i = 0; i < strs.length; i++) {
			System.out.println(list.get(i)); // 支持
		}
		// list.remove(0);// 不支持  
		// list.add("d"); // 不支持  
		
		
		// java.util.Arrays.ArrayList.ArrayList<T>(T[] array)
	}

}
