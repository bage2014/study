package com.bage.study.best.practice.service;

import com.bage.study.best.practice.model.User;

import java.util.List;

public interface UserService {

    List<User> query(String phone);

    List<User> queryByAddress(String address);

    List<User> queryPhoneLike(String key);

    int insert(User user);

    int insertBatch(List<User> userList);
}
