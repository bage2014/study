package com.bage.study.java;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapStudy {

	public static void main(String[] args) {
		
		Map<String,Object> map = new HashMap<String,Object>() ;
		System.out.println(map);
		
		Map<String,Object> concurrentHashMap = new ConcurrentHashMap<String,Object>() ;
		
		System.out.println(concurrentHashMap);

	}
	
}
