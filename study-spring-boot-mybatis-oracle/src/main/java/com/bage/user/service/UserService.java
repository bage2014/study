package com.bage.user.service;

import com.bage.user.domain.User;

import java.util.List;

public interface UserService {

	User getUserById(String id);
	
	List<User> queryAll();
	
	User queryOne(String id);

}