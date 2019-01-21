package com.bage.study.spring.boot.oauth2.temp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserByName(String name) {
        return userDao.queryByName(name);
    }

    public List<User> queryAll() {
        return userDao.queryAll();
    }

}
