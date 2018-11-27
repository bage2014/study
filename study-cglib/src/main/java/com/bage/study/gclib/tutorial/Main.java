package com.bage.study.gclib.tutorial;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class Main {

	public static void main(String[] args) {
		
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(UserService.class);
		enhancer.setCallback(new MethodInterceptor() {
			
			@Override
			public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
				
				boolean isAnnotationPresent = method.isAnnotationPresent(MyMethod.class);

				if(isAnnotationPresent) {
					System.out.println("before Annotations MyMethod is work : " + method.getName());
				}
				Object returnObj = proxy.invokeSuper(obj, args);
				if(isAnnotationPresent) {
					System.out.println("after Annotations MyMethod is work : " + method.getName());
				}
				return returnObj;
			}
		});
		UserService userService = (UserService) enhancer.create();
		userService.doSomething1("hello world");
		System.out.println();
		userService.doSomething2("hello world");
		System.out.println();
		userService.doSomething3("hello world");
		
	}
	
}
