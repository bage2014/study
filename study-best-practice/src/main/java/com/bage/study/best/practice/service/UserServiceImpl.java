package com.bage.study.best.practice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bage.study.best.practice.model.User;

import java.util.List;

@Component
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> queryUser(String phone) {
        return userMapper.;
    }
}
