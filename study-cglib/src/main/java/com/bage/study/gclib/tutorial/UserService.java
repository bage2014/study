package com.bage.study.gclib.tutorial;


public class UserService {

	@MyMethod
	public Object doSomething1(Object param) {
		System.out.println("@MyMethod.doSomething1 is work:" + param);
		return param;
	}

	public Object doSomething2(Object param) {
		System.out.println("@MyMethod.doSomething2 is work:" + param);
		return param;
	}
	
	@MyMethod
	public Object doSomething3(Object param) {
		System.out.println("@MyMethod.doSomething3 is work:" + param);
		return param;
	}
}
