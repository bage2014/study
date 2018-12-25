package com.bage.study.mybatis.springboot.user.service;

import com.bage.study.mybatis.springboot.user.domain.User;

import java.util.List;

public interface UserService {

	User getUserById(String id);
	
	List<User> queryAll();

}