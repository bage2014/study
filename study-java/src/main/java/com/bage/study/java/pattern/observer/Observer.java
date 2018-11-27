package com.bage.study.java.pattern.observer;

/**
 * 观察者接口
 * @author bage
 *
 */
public interface Observer {

	public String change(Object data);
	
	public Subject getSubject();

	public void setSubject(Subject subject);
}
