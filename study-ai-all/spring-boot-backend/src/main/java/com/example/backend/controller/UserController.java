package com.example.backend.controller;

import com.example.backend.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World! Welcome to Spring Boot Backend Service";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "张三", "zhangsan@example.com"));
        users.add(new User(2L, "李四", "lisi@example.com"));
        users.add(new User(3L, "王五", "wangwu@example.com"));
        users.add(new User(4L, "赵六", "zhaoliu@example.com"));
        users.add(new User(5L, "孙七", "sunqi@example.com"));
        return users;
    }
}
