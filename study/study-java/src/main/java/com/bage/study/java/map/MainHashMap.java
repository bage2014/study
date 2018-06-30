package com.bage.study.java.map;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MainHashMap {

	public static void main(String[] args) {
		
		Map<String,String> map = new HashMap<String,String>();
		// null
		map.put(null, null);
		System.out.println(map.get(null));
		
//		map = new Hashtable<String,String>(); 
//		// null
//		map.put(null, null);// Neither the key nor the value can be null. 
//		System.out.println(map.get(null));
		
		// put 解析
		map.put(null, null); 
//		对key的hashCode做hash，然后计算index
//		如果没有冲突，则直接放到对应index的bucket里
//		如果有冲突，以链表形式存放在buckets后
//		如果冲突导致链表过长(大于等于TREEIFY_THRESHOLD)，就把链表转换成红黑树
//		如果节点已经存在就替换old value
//		如果bucket满了(超过load factor*current capacity)，就要resize。
		
		// get解析
		map.get(null);
//		如果是bucket里的第一个节点，直接取出； 
//		如果有冲突，则通过key.equals(k)去查找对应的entry 
//		若为树，则在树中通过key.equals(k)查找，O(logn)； 
//		若为链表，则在链表中通过key.equals(k)查找，O(n)。
		
		System.out.println(map);
		//map.put(key, value);
	}
	
}
