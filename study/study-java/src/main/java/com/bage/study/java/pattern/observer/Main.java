package com.bage.study.java.pattern.observer;

import com.bage.study.java.pattern.observer.officialdemo.Observer3;
import com.bage.study.java.pattern.observer.officialdemo.SubjectImp;

/**
 * 观察者模式<br>
 * <br>
 * 在对象之间定义了一对多的依赖，这样一来，当一个对象改变状态，依赖它的对象会收到通知并自动更新。<br>
 * 即：发布订阅模式，发布者发布信息，订阅者获取信息，订阅了就能收到信息，没订阅就收不到信息<br>
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
		// subject.unbindObservers(observer1);
		
		subject.updateData("newData2");
		
		// 官方demo
		System.out.println("\n----- 官方demo ------------------");

		SubjectImp observable = new SubjectImp();
		java.util.Observer observer3 = new Observer3(); // 观察者1
		observable.addObserver(observer3);
		
		observable.setChanged();
		observable.notifyObservers("123"); // observable.notifyObservers();
		
	}
	
}
