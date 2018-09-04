package com.bage.study.java.pattern.proxy;

public class RealSubject implements Subject{

	@Override
	public void doSomething() {
		System.out.println("RealSubject doSomething");
	}

}
