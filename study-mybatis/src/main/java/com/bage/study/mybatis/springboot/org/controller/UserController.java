package com.bage.study.mybatis.springboot.org.controller;

import java.util.Arrays;
import java.util.List;

import com.bage.study.mybatis.springboot.org.dao.UserMapper;
import com.bage.study.mybatis.springboot.org.domain.Sex;
import com.bage.study.mybatis.springboot.org.domain.User;
import com.github.pagehelper.PageHelper;
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
		user.setId(2L);
		user.setName("zhangsan");
		user.setSex(Sex.Unknown);
		user.setDepartmentId(1L);
		int res = userMapper.insert(user);

		System.out.println("验证主键回写：：" + user);

		return res;
	}

	@RequestMapping("/batchInsert")
	@ResponseBody
	public Integer batchInsert() {
		User user2 = new User();
		user2.setId(3434L);
		user2.setName("zhangsan33");
		user2.setSex(Sex.Male);
		user2.setDepartmentId(3L);

		User user = new User();
		user.setId(2L);
		user.setName("zhangsan");
		user.setSex(Sex.Male);
		user.setDepartmentId(1L);

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
		user.setId(2L);
		user.setName("zhangsan-new");
		user.setSex(Sex.Famale);
		user.setDepartmentId(3L);
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
		// 当前行的下一行分页信息生效
		PageHelper.startPage(1, 2);
		List<User> users = userMapper.queryAll();
        System.out.println(users);
        return users;
	}
}
