package com.bage.study.mybatis.common.dao;

import com.bage.study.mybatis.common.domain.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    List<Department> queryAll();

}
