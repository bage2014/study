package com.bage.study.mybatis.common.domain;

import lombok.Data;

@Data
public class User {

    private Long id;
    private String name;
	private Sex sex;

	private Long departmentId;
}