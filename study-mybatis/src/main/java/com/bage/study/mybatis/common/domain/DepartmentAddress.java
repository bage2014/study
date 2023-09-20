package com.bage.study.mybatis.common.domain;

import lombok.Data;

@Data
public class DepartmentAddress {

    private Long id;
    private String name;

    private Long departmentId;
}
