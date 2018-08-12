package com.bage.study.java.pattern.observer;

/**
 * 观察者模式
 * @author bage
 *
 */
public class Main {

	public static void main(String[] args) {
		
		Subject subject = new Subject();
		Observer observer1 = new Observer1(subject);
		Observer observer2 = new Observer2(subject);
		subject.bindObservers(observer1  );
		subject.bindObservers(observer2  );

		subject.updateData("newData1");
		
		System.out.println("-----------------------");
		
		observer1.getSubject().unbindObservers(observer1);
		
		subject.updateData("newData2");
		
	}
	
}
