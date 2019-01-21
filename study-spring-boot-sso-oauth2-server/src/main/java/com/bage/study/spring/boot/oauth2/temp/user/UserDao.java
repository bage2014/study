package com.bage.study.spring.boot.oauth2.temp.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class UserDao {

    private List<User> users = new ArrayList<>();

    @Autowired
    PasswordEncoder passwordEncoder;

    {
        users.add(new User(Long.valueOf(1),"zhangsan",passwordEncoder.encode("123"), Arrays.asList(new Role(Long.valueOf(1),"USER"))));
        users.add(new User(Long.valueOf(2),"lisi",passwordEncoder.encode("123"), Arrays.asList(new Role(Long.valueOf(2),"USER"))));
        users.add(new User(Long.valueOf(3),"wangwu",passwordEncoder.encode("123"), Arrays.asList(new Role(Long.valueOf(3),"USER"))));
        users.add(new User(Long.valueOf(4),"bage",passwordEncoder.encode("bage"), Arrays.asList(new Role(Long.valueOf(4),"ADMIN"))));
    }

    public User queryByName(String name) {
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getName().equals(name)){
                return users.get(i);
            }
        }
        return null;
    }

    public List<User> queryAll() {
        return users;
    }
}
