package com.bage.study.java.map;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap 使用
 * @author bage
 *
 */
public class ConcurrentHashMapM {

	public static void main(String[] args) {
		ConcurrentHashMap<String,String> map = new ConcurrentHashMap<String,String>();
		map.put("", ""); // 构造链表或者红黑树时候的增加了synchronized
		map.size();
	}
	
}
