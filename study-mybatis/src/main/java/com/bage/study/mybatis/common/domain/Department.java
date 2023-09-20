package com.bage.study.mybatis.common.domain;

import lombok.Data;

import java.util.List;

/**
 * 多对一
 */
@Data
public class Department {

    private long id;
    private String name;

    private DepartmentAddress departmentAddress;

    List<User> users;

}
