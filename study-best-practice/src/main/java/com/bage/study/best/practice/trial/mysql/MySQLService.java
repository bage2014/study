package com.bage.study.best.practice.trial.mysql;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class MySQLService {

    @Autowired
    private UserService userService;

    public List<User> sqlSlow(String key) {
        // 地址查询 不会命中索引
        return userService.queryByAddress(key);
    }

    public List<User> ioHigh(String key) {
        return null;
    }

    public List<User> memoryHigh(String key) {
        return null;
    }

    public List<User> highActiveThread(String key) {
        return null;
    }

    public List<User> queryByKey(String key) {
        return null;
    }

    public List<User> query100(String key) {
        // 地址查询 不会命中索引
        return userService.queryPhoneLike(key);
    }

    public List<User> query10(String key) {
        // 地址查询 不会命中索引
        return userService.queryPhoneLike(key);
    }
}
