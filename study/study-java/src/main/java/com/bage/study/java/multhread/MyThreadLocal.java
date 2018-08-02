package com.bage.study.java.multhread;

import com.bage.study.java.multhread.officialdemo.ThreadId;

/**
 * ThreadLocal
 * @author bage
 *
 */
public class MyThreadLocal {

	public static void main(String[] args) {
		
		System.out.println(ThreadId.get());
		System.out.println(ThreadId.get());
		System.out.println(ThreadId.get());
		
		
	}
	
}
