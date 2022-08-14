package com.bage.study.mybatis.springboot.org.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bage.study.mybatis.springboot.org.dao.UserExtMapper;
import com.bage.study.mybatis.springboot.org.dao.UserMapper;
import com.bage.study.mybatis.springboot.org.domain.Sex;
import com.bage.study.mybatis.springboot.org.domain.User;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	UserMapper userMapper;
	@Autowired
	UserExtMapper userExtMapper;
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;
	
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

	@RequestMapping("/batchInsert2")
	@ResponseBody
	public Long batchInsert2() {
		long start = System.currentTimeMillis();
		List<User> users = new ArrayList<>();
		SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);

		UserMapper mapper = sqlSession.getMapper(UserMapper.class);
		for (int i = 0; i < 10000; i++) {
			User user2 = new User();
			user2.setId((long) (1000 + i));
			user2.setName("zhangsan" + i);
			user2.setSex(Sex.Male);
			user2.setDepartmentId(3L);

			int res = mapper.insert(user2);
			System.out.println("验证主键回写：：" + res);

		}
		sqlSession.commit();

		return System.currentTimeMillis() - start;
	}

	@RequestMapping("/batchInsert3")
	@ResponseBody
	public Long batchInsert3() {
		long start = System.currentTimeMillis();
		List<User> users = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			User user2 = new User();
			user2.setId((long) (1000 + i));
			user2.setName("zhangsan" + i);
			user2.setSex(Sex.Male);
			user2.setDepartmentId(3L);

			int res = userMapper.insert(user2);
			System.out.println("验证主键回写：：" + res);

		}
		return System.currentTimeMillis() - start;
	}

	@RequestMapping("/batchInsert")
	@ResponseBody
	public Long batchInsert() {
		long start = System.currentTimeMillis();
		List<User> users = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			User user2 = new User();
			user2.setId((long) (1000 + i));
			user2.setName("zhangsan" + i);
			user2.setSex(Sex.Male);
			user2.setDepartmentId(3L);

			users.add(user2);
		}
        int res = userMapper.batchInsert(users);
		System.out.println("验证主键回写：：" + users);
		return System.currentTimeMillis() - start;
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

	@RequestMapping("/queryByDepartmentId2")
	@ResponseBody
	public List<User> queryByDepartmentId2() {
	    long departmentId = 1L;
		return userExtMapper.queryByDepartmentId2(departmentId);
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
