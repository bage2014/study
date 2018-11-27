package com.bage.study.java.java8;

import java.util.Optional;

public class MyOptional {

	public static void main(String[] args) {
		
		Optional<Integer> opt = Optional.ofNullable(null);
		System.out.println(opt.get());
	}
}
