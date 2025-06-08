package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://www.jianshu.com/p/d98d6cb61841
 */
@RequestMapping("/mybatis")
@RestController
@Slf4j
public class MybatisController {
    @Autowired
    private UserService userService;

    @RequestMapping("/cache1")
    public Object cache1(@RequestParam("phone") String phone) {
        log.info("UserController query phone = {}", phone);
        List<User> users = userService.query(phone);
        log.info("UserController query users = {}", users);
        return users;
    }


    @RequestMapping("/cache2")
    public Object cache2(@RequestParam("phone") String phone) {
        log.info("UserController query phone = {}", phone);
        List<User> users = userService.query(phone);
        log.info("UserController query users = {}", users);
        return users;
    }
}
