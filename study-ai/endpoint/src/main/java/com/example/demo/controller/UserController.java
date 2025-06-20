package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "登录成功";
        } else {
            return "用户名或密码错误";
        }
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        if (userRepository.findByUsername(username) != null) {
            return "用户名已存在";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
        return "注册成功";
    }
}