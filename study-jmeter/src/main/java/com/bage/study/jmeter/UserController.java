package com.bage.study.jmeter;

import com.bage.study.jmeter.repo.UserEntity;
import com.bage.study.jmeter.repo.UserMapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Random;

@RestController("user")
public class UserController {

    @Autowired
    private UserMapper userMapper;
    private Random random = new Random();

    @RequestMapping("/insert")
    public Object insert(@RequestParam("append") String append) {
        UserEntity user = new UserEntity();
        user.setId(System.currentTimeMillis());
        user.setAge(random.nextInt(100));
        user.setEmail(random.nextInt(100000) + "@qq.com");
        user.setName("name:" + append);
        int result = userMapper.insert(user);
        return result;
    }

    @RequestMapping("/query")
    public Object query(@RequestParam("append") String append) {
//        Wrapper<UserEntity> wrapper = new UserEntity();
//        int result = userMapper.selectList(wrapper);
//        return result;
        return null;
    }

}
