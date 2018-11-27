package com.bage.study.java.in;

import java.util.ArrayList;
import java.util.List;

public class ArrayListUpdate {
	
	public static void main(String[] args) {
		
		List<String> list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		
		// 不支持,增强 For不能删除
		for (String item : list) {
			list.remove(item);	
		}
				
		// 支持
		for (int i = 0; i < list.size(); i++) {
			list.remove(0);			
		}
		
	}
	
}
