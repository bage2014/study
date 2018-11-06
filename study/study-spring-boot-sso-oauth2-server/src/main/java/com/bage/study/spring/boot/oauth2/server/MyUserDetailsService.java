package com.bage.study.spring.boot.oauth2.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = null;
		try {
	      user = userService.loadUserByUsername(username);
	    } catch (Exception e) {
	      throw new UsernameNotFoundException("user select fail");
	    }
	    if(user == null){
	      throw new UsernameNotFoundException("no user found");
	    } else {
	      try {
	        // String roles = "USER";// userService.getRoleByUser(user); 
	        return user;
	      } catch (Exception e) {
	        throw new UsernameNotFoundException("user role select fail");
	      }
	    }
	}

}
