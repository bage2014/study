package com.bage.study.materialize.spring.boot.organization.user.dao;

import com.bage.study.materialize.spring.boot.organization.user.domain.User;

public interface UserDao {

    User getUserById(String id);

}