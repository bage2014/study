package com.bage.user.dao;


import com.bage.user.domain.User;

public interface UserDao {

    User getUserById(String id);

}