package com.bage.study.best.practice.service;

import com.bage.study.best.practice.mapping.UserMapping;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.repo.UserEntity;
import com.bage.study.best.practice.repo.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserMapping userMapping;

    @Override
    public List<User> query(String phone) {
        LambdaQueryWrapper<UserEntity> ge = Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getPhone, phone);
        List<UserEntity> list = userMapper.selectList(ge);
        if (Objects.isNull(list)) {
            return new ArrayList<>();
        }
        return list.stream()
                .map(item -> userMapping.mapping(item))
                .collect(Collectors.toList());
    }

    @Override
    public int insert(User user) {
//        try {
//            Thread.sleep(new Random().nextInt(1000));
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return 0;
        return userMapper.insert(userMapping.mapping(user));
    }
}
