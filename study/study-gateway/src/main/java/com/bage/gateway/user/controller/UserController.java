package com.bage.gateway.user.controller;

import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
public class UserController {
	
	@SuppressWarnings("deprecation")
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public String getUserById(@RequestParam("id") String id) {
		System.out.println( "id:" + id + "; time: " + new Date().toLocaleString());
		return id;
	}
	
}
