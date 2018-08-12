package com.bage.study.java.pattern.observer.officialdemo;

import java.util.Observable;
import java.util.Observer;

public class Observer4 implements Observer{

	@Override
	public void update(Observable o, Object arg) {

		System.out.println("officialdemo ===> Observer4.update(Observable o, Object arg) is work:" + arg);
	}

}
