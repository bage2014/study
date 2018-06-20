package com.bage.controller;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bage.constant.Constants;
import com.bage.service.UserService;
import com.bage.utils.JWTUtils;
import com.bage.utils.RedisUtils;

import io.jsonwebtoken.impl.crypto.MacProvider;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@RequestMapping(value="login",method=RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request){
		
		String account = request.getParameter("account");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("account", account);
		map.put("password", request.getParameter("password"));
		map = userService.query(map);
		
		if(map != null ) { //登录成功
			request.getSession().setAttribute(Constants.session_attribute_currentuser,map.get("account"));
			
			Key key = MacProvider.generateKey();;
			String jws = JWTUtils.compactJws(account,key );
			
			System.out.println("jws:" + jws);
			
			RedisUtils.put(Constants.redis_key_currentuser + "_" + account, jws);
			RedisUtils.put(jws, key);
			
			return jws;
			
		} else { // 登录失败
			return "";
		}
	}
	
	@RequestMapping("index")
	public String index(HttpServletRequest request){
		
		return "user/index";
	}
	
	@RequestMapping("logout")
	public String logout(HttpServletRequest request){
		
		request.getSession().removeAttribute(Constants.session_attribute_currentuser);
		
		RedisUtils.clear();
		
		return "user/index";
	}
	
	@RequestMapping("me")
	@ResponseBody
	public String me(HttpServletRequest request){
		
		System.out.println("String com.bage.hello.controller.UserController.me(HttpServletRequest request) is work ");
		
		Map<String,Object> user = new HashMap<String,Object>();
		user.put("account", request.getSession().getAttribute(Constants.session_attribute_currentuser));
		
		return user.toString();
	}
	

	@RequestMapping("token")
	@ResponseBody
	public String getToken(HttpServletRequest request){
		String account = String.valueOf(request.getSession().getAttribute(Constants.session_attribute_currentuser));

		return RedisUtils.getString(Constants.redis_key_currentuser + "_" + account);
	}
}
