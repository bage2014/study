package com.bage.study.spring.boot.security;

import com.bage.study.spring.boot.security.domain.Role;
import com.bage.study.spring.boot.security.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class UserService{

	List<User> users = new ArrayList<>();
	public UserService(){
		User user = new User("admin","admin");
		user.setAuthorities(Arrays.asList(new Role("ROLE_ADMIN")));
		users.add(user);

		user = new User("org","org");
		user.setAuthorities(Arrays.asList(new Role("ROLE_USER")));
		users.add(user);

		user = new User("dba","dba");
		user.setAuthorities(Arrays.asList(new Role("ROLE_ADMIN"),new Role("ROLE_DBA")));
		users.add(user);

		user = new User("bage","bage");
		user.setAuthorities(Arrays.asList(new Role("ROLE_BAGE")));
		users.add(user);
	}

	public User loadUserByUsername(String username) {
		for(User u : users){
			if(u.getUsername().equals(username)){
				return u;
			}
		}
		return null;
	}
	
	public boolean isUserLoginOk(String username,String password){
		for(User u : users){
			if(u.getUsername().equals(username)){
				return u.getPassword().equals(password);
			}
		}
		return false;
	}
	
}
