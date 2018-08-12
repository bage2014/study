package com.bage.study.java.pattern.observer.officialdemo;

import java.util.Observable;

public class SubjectImp extends Observable{

	public synchronized void setChanged() {
		super.setChanged();
	}
}
