package com.bage.study.java.pattern.proxy;

/**
 * 代理模式
 * 参考链接：https://www.cnblogs.com/daniels/p/8242592.html
 * 
 * @author bage
 *
 */
public class Proxy {

	Subject subject;
	
	public Proxy(Subject subject){
		this.subject = subject;
	}
	
	public void doSomething(){
		subject.doSomething();
	}
	
}
