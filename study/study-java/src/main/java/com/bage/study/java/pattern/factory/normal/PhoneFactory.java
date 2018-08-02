package com.bage.study.java.pattern.factory.normal;

import com.bage.study.java.pattern.factory.Phone;
import com.bage.study.java.pattern.factory.Product;

public class PhoneFactory implements IFactory{

	@Override
	public Product getProduct() {
		return new Phone();
	}

}
