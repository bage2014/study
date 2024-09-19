package com.bage.study.lombok;

public class UserBuilderTest {

	public static void main(String[] args) {
		User bbb = User.builder()
				.id(1234L)
				.name("bbb")
				.hobby("hhh")
				.hobby("hhh3")
				.build();
		System.out.println(bbb);
	}
}
