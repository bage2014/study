package com.bage.study.spring.boot.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	UserService userService;
	
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		System.out.println("authenticate........");
		String username = authentication.getName();
	    String password = (String) authentication.getCredentials();
	    User user = userService.loadUserByUsername(username);
	    if(user == null){
	      throw new BadCredentialsException("Username not found.");
	    }
	 
	    //加密过程在这里体现
	    if (!password.equals(user.getPassword())) {
	      throw new BadCredentialsException("Wrong password.");
	    }
	 
	    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
	    return new UsernamePasswordAuthenticationToken(user, password, authorities);
	 
	}

	public boolean supports(Class<?> authentication) {
		System.out.println("supports........");

		return true;
	}

}
