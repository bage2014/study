package com.bage.study.java.pattern.observer;

public class Observer1 implements Observer {

	private Subject subject;
	
	public Observer1(Subject subject) {
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
		System.out.println("Observer1 got data from Subject is : " + data);
		return "Observer1.change(Object data) is work";
	}

}
