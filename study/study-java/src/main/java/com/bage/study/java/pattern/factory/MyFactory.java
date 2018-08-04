package com.bage.study.java.pattern.factory;

import com.bage.study.java.pattern.factory.abstr.AbstractFactory;
import com.bage.study.java.pattern.factory.abstr.HuaweiFactory;
import com.bage.study.java.pattern.factory.abstr.SanxingFactory;
import com.bage.study.java.pattern.factory.normal.ComputerFactory;
import com.bage.study.java.pattern.factory.normal.PhoneFactory;
import com.bage.study.java.pattern.factory.simple.ProductFactory;
import com.bage.study.java.pattern.factory.simple.ProductFactory.ProductType;

/**
 * 工厂模式是 Java 中最常用的设计模式之一。这种类型的设计模式属于创建型模式，它提供了一种创建对象的最佳方式。<br>
        工厂模式主要是为创建对象提供过渡接口，以便将创建对象的具体过程屏蔽隔离起来，达到提高灵活性的目的。
 * @author bage
 *
 */
public class MyFactory {

	public static void main(String[] args) {
		// 参考例子(静态工厂类)
		System.out.println("------简单工厂模式(静态工厂)-----------");
		Product phone = ProductFactory.getProduct(ProductType.Phone);
		System.out.println(phone.getName());
		Product computer = ProductFactory.getProduct(ProductType.Computer);
		System.out.println(computer.getName());
		
		// 不同方法构造
		System.out.println("------工厂方法模式-----------");
		phone = ProductFactory.getProductPhone();
		System.out.println(phone.getName());
		computer = ProductFactory.getProductComputer();
		System.out.println(computer.getName());
		
		// 普通工厂
		System.out.println("------普通工厂模式-----------");
		System.out.println(new PhoneFactory().getProduct().getName());
		System.out.println(new ComputerFactory().getProduct().getName());

		// 抽象工厂
		System.out.println("------抽象工厂模式-----------");
		AbstractFactory abstractFactory = new HuaweiFactory();
		System.out.println(abstractFactory.getPhoneProudct().getName());
		System.out.println(new HuaweiFactory().getComputerProudct().getName());
		System.out.println(new SanxingFactory().getPhoneProudct().getName());
		System.out.println(new SanxingFactory().getComputerProudct().getName());

				
		
	}
	
}
