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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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

	@RequestMapping("index.html")
	public String indexHtml(HttpServletRequest request){
		
		return "user/index";
	}
	
	@RequestMapping("logout")
	public String logout(HttpServletRequest request){
		
		RedisUtils.clear();
		
		return "redirect:/";
	}
	
	@RequestMapping("me")
	@ResponseBody
	public String me(HttpServletRequest request){
		
		Map<String,Object> user = new HashMap<String,Object>();
		String compactJws = request.getHeader("Authorization");
		Key key = (Key) RedisUtils.get(compactJws);
		Jws<Claims> jws = JWTUtils.parse(compactJws,key);
		String sub = jws.getBody().getSubject();
		
		user.put("account", sub);
		
		return user.toString();
	}
	

}
