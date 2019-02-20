package com.bage.user.service;

import java.util.List;

import com.bage.user.dao.UserDao;
import com.bage.user.dao.UserMapper;
import com.bage.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImp implements UserService {

	@Autowired
	UserDao userDao;
	@Autowired
	UserMapper userMapper;
	
	public User getUserById(String id){
		return userDao.getUserById(id);
	}

	public List<User> queryAll() {
		return userMapper.queryAll();
	}

	public User queryOne(String id) {
		return userMapper.queryOne(id);
	}
}
