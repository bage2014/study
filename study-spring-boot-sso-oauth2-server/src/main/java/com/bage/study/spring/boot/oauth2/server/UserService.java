package com.bage.study.spring.boot.oauth2.server;

import org.springframework.stereotype.Service;

@Service
public class UserService{

	public User loadUserByUsername(String username) {
		if("zhangsan".equals(username)) {
			return new User(username,"123");
		}
		if("lisi".equals(username)) {
			return new User(username,"456");
		}
		return null;
	}
	
	public boolean isUserLoginOk(String username,String password){
		if("zhangsan".equals(username) && "123".equals(password)) {
			return true;
		}
		if("lisi".equals(username) && "456".equals(password)) {
			return true;
		}
		return false;
	}
	
}
