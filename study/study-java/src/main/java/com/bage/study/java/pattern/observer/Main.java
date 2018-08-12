package com.bage.study.java.pattern.observer;

/**
 * 观察者模式<br>
 * <br>
 * 在对象之间定义了一对多的依赖，这样一来，当一个对象改变状态，依赖它的对象会收到通知并自动更新。<br>
 * 
 * @author bage
 *
 */
public class Main {

	public static void main(String[] args) {
		
		Subject subject = new ConcreteSubject(); // 被观察者
		
		Observer observer1 = new Observer1(subject); // 观察者1
		Observer observer2 = new Observer2(subject); // 观察者2
		subject.bindObservers(observer1  );
		subject.bindObservers(observer2  );

		subject.updateData("newData1");
		
		System.out.println("-----------------------");
		
		observer1.getSubject().unbindObservers(observer1);
		
		subject.updateData("newData2");
		
		
	}
	
}
