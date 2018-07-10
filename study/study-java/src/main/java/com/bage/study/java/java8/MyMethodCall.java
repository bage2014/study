package com.bage.study.java.java8;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法引用<br>
 * list.forEach(MyMethodCall::work); // 使用 :: 代替 . 省略参数<br>
 * list.forEach(call::work2); // 使用 :: 代替 . 省略参数<br>
 * 方法引用是用来直接访问类或者实例的已经存在的方法或者构造方法。<br>
 * 方法引用提供了一种引用而不执行方法的方式，它需要由兼容的函数式接口构成的目标类型上下文
 * @author bage
 *
 */
public class MyMethodCall {

	public static void main(String[] args) {
		List<String> list= new ArrayList<String>();
		list.add("a");
		list.add("b");
		list.add("c");
		
		MyMethodCall call =  new MyMethodCall();
		
		list.forEach(MyMethodCall::work); // 使用 :: 代替 . 省略参数
		list.forEach(call::work2); // 使用 :: 代替 . 省略参数
	}
	public static void work(String str) {
		System.out.println(str);		
	}
	
	public void work2(String str) {
		System.out.println(str);		
	}
	
	
}
