package com.bage.study.mybatis.springboot.user.dao;

import com.bage.study.mybatis.springboot.user.domain.User;

public interface UserDao {

    User getUserById(String id);

}