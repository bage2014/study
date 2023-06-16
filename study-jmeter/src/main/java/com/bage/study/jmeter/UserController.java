package com.bage.study.jmeter;

import com.bage.study.jmeter.repo.UserEntity;
import com.bage.study.jmeter.repo.UserMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping(("/user"))
public class UserController {

    @Autowired
    private UserMapper userMapper;
    private Random random = new Random();

    @RequestMapping("/insert")
    public Object insert(@RequestParam("name") String name) {
        UserEntity user = new UserEntity();
        user.setId(System.currentTimeMillis());
        user.setAge(random.nextInt(100));
        user.setEmail(random.nextInt(100000) + "@qq.com");
        user.setName(name);
        int result = userMapper.insert(user);
        return result;
    }

    @RequestMapping("/query")
    public Object query(@RequestParam("name") String name) {
        LambdaQueryWrapper<UserEntity> select =
                Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getName, name);
        List<UserEntity> result = userMapper.selectList(select);
        return result;
    }
    @RequestMapping("/query2")
    public Object query2(@RequestParam("name") String name) {
        LambdaQueryWrapper<UserEntity> select =
                Wrappers.<UserEntity>lambdaQuery().eq(UserEntity::getName, name);
        List<UserEntity> result = userMapper.selectList(select);
        return "{\"resultCode\":200}";
    }

}
