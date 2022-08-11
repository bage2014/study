package com.bage.study.mybatis.springboot.org.dao;

import com.bage.study.mybatis.springboot.org.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserExtMapper extends UserMapper {

    List<User> queryByDepartmentId2(long departmentId);

}
