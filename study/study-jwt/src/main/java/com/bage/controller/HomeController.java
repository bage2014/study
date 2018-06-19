package com.bage.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bage.constant.Constants;

@Controller
public class HomeController {

	@RequestMapping(value="")
	public String loin(HttpServletRequest request){
		
		Object account = request.getSession().getAttribute(Constants.session_attribute_currentuser);
		if(account == null) {
			return "user/login";
		}
		return "user/index";
	}
	
}
