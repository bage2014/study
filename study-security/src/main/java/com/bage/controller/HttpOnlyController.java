package com.bage.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/httponly")
public class HttpOnlyController {

	@RequestMapping("/index")
	public String index(HttpServletRequest request,HttpServletResponse response) {
		
		Cookie cookie = new Cookie("httponlyCookie", "httponlyValue");
		response.addCookie(cookie );
		
		return "httponly/index";
	}
	
}