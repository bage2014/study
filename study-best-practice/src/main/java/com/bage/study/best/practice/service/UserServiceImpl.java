package com.bage.study.best.practice.service;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.bage.study.best.practice.mapping.UserMapping;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.repo.UserEntity;
import com.bage.study.best.practice.repo.UserEntityMapper;
import com.bage.study.best.practice.repo.UserEntityRepoServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserEntityMapper userMapper;
    @Autowired
    private UserMapping userMapping;
    @Autowired
    private UserEntityRepoServiceImpl userEntityRepoService;

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
    public List<User> queryByAddress(String address) {
        LambdaQueryWrapper<UserEntity> ge = Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getAddress, address);
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
        try (Entry entry = SphU.entry("HelloWorld")) {
            // 被保护的逻辑
            return userMapper.insert(userMapping.mapping(user));
        } catch (BlockException ex) {
            // 处理被流控的逻辑
            System.out.println("blocked!");
            throw new RuntimeException("blocked!");
        }
    }

    @Override
    public int insertBatch(List<User> userList) {
        if(userList == null){
            return 0;
        }
        try (Entry entry = SphU.entry("HelloWorld")) {
            // 被保护的逻辑
            boolean result = userEntityRepoService.saveBatch(userMapping.mapping(userList));
            return result ? userList.size() : 0;
        } catch (BlockException ex) {
            // 处理被流控的逻辑
            System.out.println("blocked!");
            throw new RuntimeException("blocked!");
        }
    }
}
