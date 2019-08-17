package com.bage.study.springboot.aop.demo;

import com.bage.study.springboot.aop.annotation.crypt.WithMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserService {

    List<User> users = new ArrayList<User>();

    @WithMethod(before = true)
    public void insert(User user) {
        users.add(user);
    }

    @WithMethod(afterReturning = true)
    public List<User> queryAll() {
        return users;
    }

    public List<User> getUsers() {
        return users;
    }
}

