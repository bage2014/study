package com.bage.study.mybatis.springboot.org.controller;

import java.util.Arrays;
import java.util.List;

import com.bage.study.mybatis.springboot.org.dao.UserMapper;
import com.bage.study.mybatis.springboot.org.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserMapper userMapper;
	
	@RequestMapping("/insert")
	@ResponseBody
	public Integer insert() {
		User user = new User();
		user.setId(2);
		user.setName("zhangsan");
		user.setSex("M");
		user.setDepartmentId(1);
		int res = userMapper.insert(user);

		System.out.println("验证主键回写：：" + user);

		return res;
	}

	@RequestMapping("/batchInsert")
	@ResponseBody
	public Integer batchInsert() {
		User user2 = new User();
		user2.setId(3434);
		user2.setName("zhangsan33");
		user2.setSex("M3");
		user2.setDepartmentId(3);

		User user = new User();
		user.setId(2);
		user.setName("zhangsan");
		user.setSex("M");
		user.setDepartmentId(1);

        List<User> users = Arrays.asList(user, user2);
        int res = userMapper.batchInsert(users);

		System.out.println("验证主键回写：：" + users);

		return res;
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Integer delete() {
		return userMapper.delete(2);
	}

	@RequestMapping("/update")
	@ResponseBody
	public Integer update() {
		User user = new User();
		user.setId(2);
		user.setName("zhangsan-new");
		user.setSex("F");
		user.setDepartmentId(3);
		return userMapper.update(user);
	}

	@RequestMapping("/queryByDepartmentId")
	@ResponseBody
	public List<User> queryByDepartmentId() {
	    long departmentId = 1L;
		return userMapper.queryByDepartmentId(departmentId);
	}
	@RequestMapping("/all")
	@ResponseBody
	public List<User> getUser() {
		return userMapper.queryAll();
	}
}
