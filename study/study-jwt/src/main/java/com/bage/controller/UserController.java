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
import com.bage.utils.DateUtils;
import com.bage.utils.JsonUtils;
import com.bage.utils.RedisUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
			
			Key key = MacProvider.generateKey();
			Map<String,Object> cliams = new HashMap<String,Object>();
			cliams.put("jti", String.valueOf(System.currentTimeMillis()));
			String jws = Jwts.builder()
					.setClaims(cliams )
					.setIssuer("com.bage") // iss: 该JWT的签发者，是否使用是可选的
					.setSubject(account) // sub: 该JWT所面向的用户，是否使用是可选的；
					//.setAudience(aud) // aud: 接收该JWT的一方，是否使用是可选的
					.setExpiration(DateUtils.getJwtsExpirationDate()) // exp(expires): 什么时候过期，这里是一个Unix时间戳，是否使用是可选的
					.setIssuedAt(DateUtils.now()) // iat(issued at): 在什么时候签发的(UNIX时间)，是否使用是可选的
					//.setNotBefore(DateUtils.getNotBeforeDate()) // nbf (Not Before)：如果当前时间在nbf里的时间之前，则Token不被接受；一般都会留一些余地，比如几分钟；是否使用是可选的
					.signWith(SignatureAlgorithm.HS512, key)
					.compact();
			
			System.out.println("jws:" + jws);
			
			RedisUtils.put(Constants.redis_key_jwt + "_" + account, jws);
			RedisUtils.put(Constants.redis_key_jwtkey + "_" + account, key);
			
			map = new HashMap<String,Object>();
			map.put("jws", jws);
			map.put("now", DateUtils.now());
			map.put("expirationDate", DateUtils.getJwtsExpirationDate());

			return JsonUtils.toJson(map);
			
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
	@ResponseBody
	public String logout(HttpServletRequest request){
		
		String claimsJws = request.getHeader("Authorization");
		// 签名验证
		try {
			
			Key key = (Key) RedisUtils.get(claimsJws);
			Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
			String sub = jws.getBody().getSubject();
			RedisUtils.clear(Constants.redis_key_jwt + "_" + sub);
			RedisUtils.clear(Constants.redis_key_jwtkey + "_" + sub);

			RedisUtils.clear(claimsJws);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	@RequestMapping(value="me")
	@ResponseBody
	public String me(HttpServletRequest request){
		
		Map<String,Object> user = new HashMap<String,Object>();
		String claimsJws = request.getHeader("Authorization");
		Key key = (Key) RedisUtils.get(claimsJws);
		Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
		String sub = jws.getBody().getSubject();
		
		user.put("account", sub);
		
		return user.toString();
	}
	

}
