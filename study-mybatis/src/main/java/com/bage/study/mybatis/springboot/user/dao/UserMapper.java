package com.bage.study.mybatis.springboot.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.bage.study.mybatis.springboot.user.domain.User;

@Mapper
public interface UserMapper {

    List<User> queryAll();

}
