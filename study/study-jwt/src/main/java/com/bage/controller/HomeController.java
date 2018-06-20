package com.bage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@RequestMapping(value="")
	public String loin(HttpServletRequest request){
		
		return "user/login";
	}
	
	@RequestMapping(value="authorization/fail")
	@ResponseBody
	public String authorizationFail(HttpServletRequest request){
		return "请先登录";
	}
	
}
