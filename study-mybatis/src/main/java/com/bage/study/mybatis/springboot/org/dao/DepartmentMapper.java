package com.bage.study.mybatis.springboot.org.dao;

import com.bage.study.mybatis.springboot.org.domain.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    List<Department> queryAll();

}
