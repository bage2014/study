package com.bage.study.java.pattern.strategy;

/**
 * Strategy 策略模式的使用<br>
 * <br>
 * 策略模式定义了一系列的算法，并将每一个算法封装起来，而且使他们可以相互替换，让算法独立于使用它的客户而独立变化<br>
 * @author bage
 *
 */
public class Main {

	public static void main(String[] args) {
		Client client1 = new Client1(); // 客户端1(客户端可以是共同实现了某超类的不同子类)
		client1.setStrategy(new StrategyImp1());
		System.out.println(client1.showStrategy());
		
		Client client2 = new Client2(); // 客户端2
		client2.setStrategy(new StrategyImp2());
		System.out.println(client2.showStrategy());
	}
	
}
