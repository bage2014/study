package com.bage.study.java.pattern.factory.abstr;

import com.bage.study.java.pattern.factory.Product;

public class HUAWEIPhoneFactory extends AbstractPhoneFactory{

	@Override
	public Product getPhoneProudct() {
		return new HuaweiPhone();
	}

	@Override
	public Product getComputerProudct() {
		return new HuaweiComputer();
	}
	
	
}
