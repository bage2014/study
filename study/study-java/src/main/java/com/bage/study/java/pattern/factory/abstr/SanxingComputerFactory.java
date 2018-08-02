package com.bage.study.java.pattern.factory.abstr;

import com.bage.study.java.pattern.factory.Product;

public class SanxingComputerFactory extends AbstractComputerFactory{

	@Override
	public Product getPhoneProudct() {
		return new SanxingPhone();
	}

	@Override
	public Product getComputerProudct() {
		return new SanxingComputer();
	}

}
