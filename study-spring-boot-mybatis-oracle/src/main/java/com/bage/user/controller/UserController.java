package com.bage.user.controller;

import com.bage.user.dao.UserMapper;
import com.bage.user.domain.User;
import com.bage.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
	UserMapper userMapper;
	
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
	

	@RequestMapping("/one/{id}")
	@ResponseBody
	public User getOneUser(@PathVariable String id) {
		return userService.queryOne(id);
	}
	
	@RequestMapping("/admin/{id}")
	@ResponseBody
	public Map<String, Object> querySys(@PathVariable String id) {
		return userMapper.querySys(id);
	}
}
