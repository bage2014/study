package com.bage.study.lombok;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class User {

	private Long id;
	private String name;

	@Singular
	private List<String> hobbies;
	
}
