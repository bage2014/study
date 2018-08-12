package com.bage.study.java.pattern.observer;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author bage
 *
 */
public class Subject {

	private List<Observer> observers = new LinkedList<>();
	
	public void bindObservers(Observer observer) {
		observers.add(observer);
	}
	
	public void unbindObservers(Observer observer) {
		if(observers.contains(observer)){
			observers.remove(observer);
		}
	}
	
	public List<Observer> getObservers() {
		return observers;
	}

	public void setObservers(List<Observer> observers) {
		this.observers = observers;
	}
	
	public void updateData(Object newData){
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).change(newData);
		}
	}
	
}
