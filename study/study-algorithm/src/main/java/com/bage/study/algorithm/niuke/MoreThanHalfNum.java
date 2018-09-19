package com.bage.study.algorithm.niuke;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MoreThanHalfNum {
	 public int MoreThanHalfNum_Solution(int [] array) {
		 Map<Integer,Integer> countMap = new HashMap<Integer,Integer>();
	    	for (int i = 0; i < array.length; i++) {
	    		Integer key = array[i];
	    		Integer value = countMap.get(key);
	    		if(value == null){
					countMap.put(key, 1);
	    		} else {
	    			countMap.put(key, ++ value);
	    		}
			}
	    	int index = 0;
	    	for (Entry<Integer, Integer> e : countMap.entrySet()) {
				if(e.getValue() * 2 > array.length){
					index = e.getKey();
				}
			}
	    	return index;
	    }
}
