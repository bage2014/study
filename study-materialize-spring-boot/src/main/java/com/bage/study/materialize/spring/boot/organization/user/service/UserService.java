package com.bage.study.materialize.spring.boot.organization.user.service;

import java.util.List;

import com.bage.study.materialize.spring.boot.organization.user.domain.User;

public interface UserService {

	User getUserById(String id);
	
	List<User> queryAll();

}