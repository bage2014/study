package com.bage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/index")
	public String index() {
		System.out.println("index");
		return "index";
	}

	@RequestMapping("/login")
	public String login(HttpServletRequest request) {
		request.getSession().setAttribute("username", "rob");
		System.out.println("login");
		return "index";
	}
	
	@RequestMapping("/get")
	public String get(HttpServletRequest request) {
		System.out.println("username=" + request.getSession().getAttribute("username"));
		return "index";
	}
	
}
