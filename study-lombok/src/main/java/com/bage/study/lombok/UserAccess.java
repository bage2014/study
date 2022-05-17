package com.bage.study.lombok;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class UserAccess {

	private String id;
	private String name;
	
}
