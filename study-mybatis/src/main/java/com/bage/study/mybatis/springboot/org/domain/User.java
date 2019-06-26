package com.bage.study.mybatis.springboot.org.domain;

import lombok.Data;

@Data
public class User {

    private long id;
    private String name;
	private String sex;

	private long departmentId;
}