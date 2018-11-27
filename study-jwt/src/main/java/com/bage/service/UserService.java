package com.bage.service;

import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class UserService {

	public Map<String,Object> query(Map<String,Object> map){
		if(map.get("account") == null) {
			return null;
		}
		if(map.get("account").equals(map.get("password"))) {
			return map;
		}
		return null;
	}
	
}
