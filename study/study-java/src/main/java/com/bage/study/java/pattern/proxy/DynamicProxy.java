package com.bage.study.java.pattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 实现 JDK的 InvocationHandler 接口
 * @author bage
 *
 */
public class DynamicProxy implements InvocationHandler{

	private Object subject;
	
	public DynamicProxy(Object subject){
		this.subject = subject;
	}
	
	/**
	 * proxy 代理对象[用的少?]
	 * method 待执行方法
	 * args 方法参数
	 * 
	 */
	@Override			
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		System.out.println(proxy); // 不能输出 proxy 否则会出现异常
//		System.out.println("method:" + method);
//		System.out.println("args:" + args);
		System.out.println("调用方法前");
		/**
		 * obj 待代理的接口实现  the object the underlying method is invoked from
		 * args 参数 the arguments used for the method call
		 */
		Object result  = method.invoke(this.subject, args);
		System.out.println("调用方法后");
		return result ;
	}

}
