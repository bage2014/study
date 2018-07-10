package com.bage.study.java.java8;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 对 Lamdba表达式的基本使用
 * @author bage
 *
 */
public class MyLamdba {
	
	public static void main(String[] args) {
		
		MyLamdba Lamdba = new MyLamdba();
		Lamdba.tutorial();
		
	}
	
	public void tutorial(){
		List<String> list = Arrays.asList(new String[]{"d","aa","c","e"});
		
		print(list);
		
		newThread();
		
		sort(list);
		
		filter(list);
		
		parallelStream(list);
	}
	
	/**
	 * 基本打印
	 * @param list
	 */
	public void print(List<String> list){
		System.out.println("打印数组--------------");
		System.out.println("------- 普通方式--------------");
		list.forEach(new Consumer<String>() {
			@Override
			public void accept(String t) {
				System.out.print(t + "\t");
			}
		});
		System.out.println();
		System.out.println("------- lamdba--------------");
		list.forEach(a -> System.out.print(a + "\t"));
		// 或者 list.forEach(a -> {System.out.println(a);});
		System.out.println();
	}
	
	/**
	 * 创建线程
	 * @param list
	 */
	public void newThread(){
		System.out.println("创建线程--------------");
		System.out.println("------- 普通方式--------------");
		new Thread(){
			public void run() {
				System.out.println("线程已经启动。。。");
			};
		}.start();
		System.out.println("------- lamdba--------------");
		new Thread(()->System.out.println("线程已经启动。。。")).start();
	}
	
	
	/**
	 * 进行排序
	 * @param list
	 */
	public void sort(List<String> list){
		System.out.println("进行排序--------------");
		System.out.println("------- 普通方式--------------");
		Collections.sort(list, new Comparator<String>(){
			@Override
			public int compare(String str1, String str2) {
				return str1.compareTo(str2);
			}
		});
		print(list);
		System.out.println("------- lamdba--------------");
		Collections.sort(list, (str1,str2) -> str1.compareTo(str2));
		// 也可以制定类型 Comparator<String> comparator = (String str1,String str2) -> str1.compareTo(str2);
		// Collections.sort(list, comparator);
		print(list);
	}
	
	/**
	 * 进行查找
	 * @param list
	 */
	public void filter(List<String> list){
		System.out.println("进行查找过滤--------------");
		System.out.println("------- 普通方式--------------");
		Predicate<String> predicate = new Predicate<String>() {
			@Override
			public boolean test(String t) {
				return t.length() <= 1;
			}
		};
		list.stream().filter(predicate ).forEach(a->System.out.print(a + "\t"));
		System.out.println();
		System.out.println("------- lamdba--------------");
		list.stream().filter(str-> str.length() > 1 ).forEach(a->System.out.print(a + "\t"));
		System.out.println();
	}
	
	
	/**
	 * 并行处理
	 * @param list
	 */
	public void parallelStream(List<String> list){
		System.out.println("进行并行处理--------------");
		System.out.println("------- lamdba--------------");
		System.out.println(list.stream().filter(a -> a.length() > 1).parallel().mapToInt(a -> a.length()).sum());
		System.out.println();
	}
	
}
