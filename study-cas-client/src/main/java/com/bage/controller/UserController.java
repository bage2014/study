package com.bage.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

	@RequestMapping("remote")
	public String remote(HttpServletRequest request){
		String user = request.getRemoteUser();
		System.out.println("org:" + user);
		
		Principal principal = request.getUserPrincipal();
		System.out.println("principal:" + principal);
		
		return "remote";
	}
	
}
