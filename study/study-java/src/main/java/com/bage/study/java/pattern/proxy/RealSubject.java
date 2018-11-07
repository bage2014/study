package com.bage.study.java.pattern.proxy;

/**
 * 接口实现
 * @author bage
 *
 */
public class RealSubject implements Subject{

	@Override
	public void doSomething() {
		System.out.println("RealSubject doSomething");
	}

}
