package com.bage.study.java.pattern.observer;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * @author bage
 *
 */
public class ConcreteSubject implements Subject {

	private List<Observer> observers = new LinkedList<>();
	
	/* (non-Javadoc)
	 * @see com.bage.study.java.pattern.observer.Subject#bindObservers(com.bage.study.java.pattern.observer.Observer)
	 */
	@Override
	public void bindObservers(Observer observer) {
		observers.add(observer);
	}
	
	/* (non-Javadoc)
	 * @see com.bage.study.java.pattern.observer.Subject#unbindObservers(com.bage.study.java.pattern.observer.Observer)
	 */
	@Override
	public void unbindObservers(Observer observer) {
		if(observers.contains(observer)){
			observers.remove(observer);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.bage.study.java.pattern.observer.Subject#getObservers()
	 */
	@Override
	public List<Observer> getObservers() {
		return observers;
	}

	/* (non-Javadoc)
	 * @see com.bage.study.java.pattern.observer.Subject#setObservers(java.util.List)
	 */
	@Override
	public void setObservers(List<Observer> observers) {
		this.observers = observers;
	}
	
	/* (non-Javadoc)
	 * @see com.bage.study.java.pattern.observer.Subject#updateData(java.lang.Object)
	 */
	@Override
	public void updateData(Object newData){
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).change(newData);
		}
	}
	
}
