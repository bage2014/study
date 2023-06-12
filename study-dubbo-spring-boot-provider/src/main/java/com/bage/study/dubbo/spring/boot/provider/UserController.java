package com.bage.study.dubbo.spring.boot.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	 @Autowired
	 UserMapper userMapper;
	 
	 @GetMapping
	 @ResponseBody
	 public Object show() {
		 return userMapper.selectList(null);
	 }
	
}
