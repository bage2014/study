package com.bage.study.java.pattern.strategy;

public class Client {

	private Strategy strategy; // 持有一个策略，根据不同上下文而不同

	public String showStrategy(){
		return strategy.doSomething();
	}
	
	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

}
