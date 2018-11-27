package com.bage.study.java.pattern.factory.abstr;

public class HuaweiFactory extends AbstractFactory{

	@Override
	public AbstractProduct getPhoneProudct() {
		return new HuaweiPhone();
	}

	@Override
	public AbstractProduct getComputerProudct() {
		return new HuaweiComputer();
	}
	
	
}
