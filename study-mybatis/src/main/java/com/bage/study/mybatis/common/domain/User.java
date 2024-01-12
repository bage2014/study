package com.bage.study.mybatis.common.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {

    private Long id;
    private String name;
	private Sex sex;

	private Long departmentId;
}