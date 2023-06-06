package com.bage.study.best.practice.controller;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/query")
    public Object query(@RequestParam("phone") String phone) {
        log.info("UserController query phone = {}", phone);
        List<User> users = userService.queryUser(phone);
        log.info("UserController query users = {}", users);
        return users;
    }

}
