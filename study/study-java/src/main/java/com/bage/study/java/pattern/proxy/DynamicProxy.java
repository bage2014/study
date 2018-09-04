package com.bage.study.java.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class DynamicProxy implements InvocationHandler{

	private Object subject;
	
	public DynamicProxy(Object subject){
		this.subject = subject;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		System.out.println(proxy); // 不能输出 proxy 否则会出现异常
//		System.out.println("method:" + method);
//		System.out.println("args:" + args);
		return method.invoke(this.subject, args);
	}

}
