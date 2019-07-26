package com.bage.study.springboot.aop;

import com.bage.util.json.JsonUtils;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {

    String dbData = "[]";

    @WithMethod(before = true)
    public void insert(User user) {
        List<User> users = JsonUtils.fromJson(dbData, new TypeToken<List<User>>() {}.getType());
        users.add(user);
        dbData = JsonUtils.toJson(users);
    }

    @WithMethod(afterReturning = true)
    public List<User> queryAll() {
        List<User> users = JsonUtils.fromJson(dbData, new TypeToken<List<User>>() {}.getType());
        return users;
    }

    public List<User> getUsers() {
        List<User> users = JsonUtils.fromJson(dbData, new TypeToken<List<User>>() {}.getType());
        return users;
    }
}

