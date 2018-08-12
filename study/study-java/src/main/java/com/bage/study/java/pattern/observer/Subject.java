package com.bage.study.java.pattern.observer;

import java.util.List;

public interface Subject {

	void bindObservers(Observer observer);

	void unbindObservers(Observer observer);

	List<Observer> getObservers();

	void setObservers(List<Observer> observers);

	void updateData(Object newData);

}