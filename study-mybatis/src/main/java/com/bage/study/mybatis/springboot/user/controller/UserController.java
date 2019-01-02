package com.bage.study.mybatis.springboot.user.controller;

import java.util.List;

import com.bage.study.mybatis.springboot.user.domain.User;
import com.bage.study.mybatis.springboot.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@RequestMapping("/{id}")
	@ResponseBody
	public User getUser(@PathVariable String id) {
		return userService.getUserById(id);
	}
	
	@RequestMapping("/all")
	@ResponseBody
	public List<User> getUser() {
		return userService.queryAll();
	}
}
