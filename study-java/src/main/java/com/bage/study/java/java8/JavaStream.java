package com.bage.study.java.java8;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Java 8的新特性
 * @author bage
 *
 */
public class JavaStream {

	
	public static void main(String[] args) {
		
		String hh = Arrays.asList("a","b").stream().collect(Collectors.joining(","));
		System.out.println(hh);
	}
}
