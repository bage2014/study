package com.bage.study.java.pattern.observer;

public class Observer2 implements Observer {

	private Subject subject; // 过一个Subject属性，可以自己注册或者取消订阅
	
	public Observer2(Subject subject) {
		super();
		this.subject = subject;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	
	@Override
	public String change(Object data) {
		System.out.println("Observer2 got data from Subject is : " + data);
		return "Observer2.change(Object data) is work";
	}

}
