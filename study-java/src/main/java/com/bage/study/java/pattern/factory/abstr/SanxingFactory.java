package com.bage.study.java.pattern.factory.abstr;

public class SanxingFactory extends AbstractFactory{

	@Override
	public AbstractProduct getPhoneProudct() {
		return new SanxingPhone();
	}

	@Override
	public AbstractProduct getComputerProudct() {
		return new SanxingComputer();
	}

}
