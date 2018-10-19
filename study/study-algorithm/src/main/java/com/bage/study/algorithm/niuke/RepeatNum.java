package com.bage.study.algorithm.niuke;

import java.util.HashMap;
import java.util.Map;

/**
 * https://www.nowcoder.com/practice/623a5ac0ea5b4e5f95552655361ae0a8?tpId=13&tqId=11203&tPage=3&rp=3&ru=%2Fta%2Fcoding-interviews&qru=%2Fta%2Fcoding-interviews%2Fquestion-ranking
 * 在一个长度为n的数组里的所有数字都在0到n-1的范围内。 数组中某些数字是重复的，但不知道有几个数字是重复的。也不知道每个数字重复几次。请找出数组中任意一个重复的数字。 例如，如果输入长度为7的数组{2,3,1,0,2,5,3}，那么对应的输出是第一个重复的数字2。
 * @author bage
 *
 */
public class RepeatNum {

	    // Parameters:
	    //    numbers:     an array of integers
	    //    length:      the length of array numbers
	    //    duplication: (Output) the duplicated number in the array number,length of duplication array is 1,so using duplication[0] = ? in implementation;
	    //                  Here duplication like pointor in C/C++, duplication[0] equal *duplication in C/C++
	    //    这里要特别注意~返回任意重复的一个，赋值duplication[0]
	    // Return value:       true if the input is valid, and there are some duplications in the array number
	    //                     otherwise false
	    public boolean duplicate(int numbers[],int length,int [] duplication) {
	    	Map<Integer,Integer> map = new HashMap<Integer,Integer>();
	    	for (int i = 0; i < length; i++) {
	    		Integer in = map.get(numbers[i]);
	    		if(in == null){
	    			map.put(numbers[i],1);
				} else {
					map.put(numbers[i],in ++);
					duplication[0] = numbers[i];
					return true;
				}
			}
	    	return false;
	    }
	
}
