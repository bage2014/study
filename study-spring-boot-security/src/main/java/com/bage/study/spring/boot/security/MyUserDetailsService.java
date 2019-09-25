package com.bage.study.spring.boot.security;

import com.bage.study.spring.boot.security.domain.User;
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
	      throw new UsernameNotFoundException("org select fail");
	    }
	    if(user == null){
	      throw new UsernameNotFoundException("no org found");
	    } else {
	      try {
	        // String roles = "USER";// userService.getRoleByUser(org);
	        return user;
	      } catch (Exception e) {
	        throw new UsernameNotFoundException("org role select fail");
	      }
	    }
	}

}
