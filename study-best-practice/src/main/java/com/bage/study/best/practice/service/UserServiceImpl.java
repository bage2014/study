package com.bage.study.best.practice.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
//        throw new RuntimeException("blocked!");

        try (Entry entry = SphU.entry("HelloWorld")) {
            // 被保护的逻辑
            return userMapper.insert(userMapping.mapping(user));
        } catch (BlockException ex) {
            // 处理被流控的逻辑
            System.out.println("blocked!");
            throw new RuntimeException("blocked!");
        }
    }
}
