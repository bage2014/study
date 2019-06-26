package com.bage.study.mybatis.springboot.org.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bage.study.mybatis.springboot.org.domain.User;

@Mapper
public interface UserMapper {

    int insert(User user);
    int batchInsert(List<User> users);

    int delete(long id);

    int update(User user);

    List<User> queryAll();
    List<User> queryByDepartmentId(long departmentId);

}
