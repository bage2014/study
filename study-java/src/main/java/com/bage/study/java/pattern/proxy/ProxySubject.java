package com.bage.study.java.pattern.proxy;

/**
 * 代理模式
 * 静态代理对象，持有代理接口实现类，同时实现代理接口
 * 参考链接：https://www.cnblogs.com/daniels/p/8242592.html
 * 
 * @author bage
 *
 */
public class ProxySubject implements Subject{

	Subject subject;
	
	public ProxySubject(Subject subject){
		this.subject = subject;
	}

	@Override
	public void doSomething() {
		this.subject.doSomething();
	}
	
}
