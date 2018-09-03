package com.bage.study.java.pattern.proxy;

public class MyProxy {

	public static void main(String[] args) {
		
		Proxy proxy = new Proxy(new RealSubject());
		proxy.doSomething();
		
	}
	
}
