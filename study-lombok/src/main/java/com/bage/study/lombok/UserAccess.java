package com.bage.study.lombok;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Getter
public class UserAccess {

	private Integer id;
	private String name;

	public static void main(String[] args) {
		UserAccess bbb = new UserAccess()
				.id(1234)
				.name("bbb");
		System.out.println(bbb.name());
	}
}
