package com.bage.study.java.pattern.factory.normal;

import com.bage.study.java.pattern.factory.Computer;
import com.bage.study.java.pattern.factory.Product;

public class ComputerFactory implements IFactory{

	@Override
	public Product getProduct() {
		return new Computer();
	}

}
