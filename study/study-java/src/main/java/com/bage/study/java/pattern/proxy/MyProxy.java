package com.bage.study.java.pattern.proxy;

import java.lang.reflect.Proxy;

/**
 * 参考链接：https://www.cnblogs.com/qifengshi/p/6566752.html<br>
 * https://www.cnblogs.com/oumyye/p/4374671.html<br>
 * https://blog.csdn.net/lovejj1994/article/details/78080124
 * 组成：抽象角色:通过接口或抽象类声明真实角色实现的业务方法。
 * 代理角色：实现抽象角色，是真实角色的代理，通过真实角色的业务逻辑方法来实现抽象方法，并可以附加自己的操作。
 * 真实角色：实现抽象角色，定义真实角色所要实现的业务逻辑，供代理角色调用。
 * 代理模式的定义：为其他对象提供一种代理以控制对这个对象的访问。在某些情况下，一个对象不适合或者不能直接引用另一个对象，而代理对象可以在客户端和目标对象之间起到中介的作用。
 * 
 * @author bage
 *
 */
public class MyProxy {

	public static void main(String[] args) {
		System.out.println("--------- 静态代理 ------------");
		// 静态代理
		ProxySubject staticProxy = new ProxySubject(new RealSubject());
		staticProxy.doSomething();

		System.out.println("--------- 动态代理 ------------");
		// 1、动态代理	
		Subject realSubject = new RealSubject(); // 创建待代理的实现类
		
		// 2、Proxy.newProxyInstance
		Subject subject = (Subject) Proxy.newProxyInstance(
				realSubject.getClass().getClassLoader(), // 类加载器 
				new Class[]{Subject.class},  // 代理接口类数组
				new DynamicProxy(realSubject)); // InvocationHandler 的实现
		
		// 调用代理对象的方法
		subject.doSomething();
	}

}
