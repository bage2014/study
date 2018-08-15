package com.bage.study.java.pattern.singleton;

/**
 * 单例模式，是一种常用的软件设计模式。在它的核心结构中只包含一个被称为单例的特殊类。<br>
 * 通过单例模式可以保证系统中一个类只有一个实例。即一个类只有一个对象实例。
 * @author bage
 *
 */
public class Singleton {

	public static void main(String[] args) {
		
		// 推荐使用SingletonDemo4
		for (int i = 0; i < 10; i++) {
			new Thread(){public void run() {
				//System.out.println(SingletonDemo5.getInstance());
				System.out.println(SingletonDemo6.INSTANCE.syso());
			};}.start();
		}
		
	}
	
	
}

/**
 * 饿汉模式（线程安全）
 * @author bage
 *
 */
class SingletonDemo{
	private static SingletonDemo singletonDemo = new SingletonDemo();
	private SingletonDemo(){
		
	}
	public static SingletonDemo getInstance(){
		return singletonDemo;
	}
}

/**
 * 懒汉形式（非线程安全）
 * @author bage
 *
 */
class SingletonDemo2{
	private static SingletonDemo2 singletonDemo = null;
	private SingletonDemo2(){
		
	}
	public static SingletonDemo2 getInstance(){
		if(singletonDemo == null){
			singletonDemo = new SingletonDemo2();
		}
		return singletonDemo;
	}
}
/**
 * 懒汉形式（线程安全、效率不太行）
 * @author bage
 *
 */
class SingletonDemo3{
	private static SingletonDemo3 singletonDemo = null;
	private SingletonDemo3(){
		
	}
	public static synchronized SingletonDemo3 getInstance(){
		if(singletonDemo == null){
			singletonDemo = new SingletonDemo3();
		}
		return singletonDemo;
	}
}
/**
 * 懒汉形式（线程安全）
 * @author bage
 *
 */
class SingletonDemo4{
	private static volatile SingletonDemo4 singletonDemo = null; // volatile 防止指令重排
	private SingletonDemo4(){
		
	}
	public static SingletonDemo4 getInstance(){
		if(singletonDemo == null){
			synchronized (SingletonDemo4.class) {
				if(singletonDemo == null){
					singletonDemo = new SingletonDemo4();
				}
			}
		}
		return singletonDemo;
	}
}
// 不支持继承
//class SingletonDemoSun extends SingletonDemo{
//	
//}

/**
 * 内部类实现方式
 * @author bage
 *
 */
class SingletonDemo5{
	
	private SingletonDemo5(){
		
	}
	public static SingletonDemo5 getInstance(){
		return SingletonDemo5InnerClass.singletonDemo5;
	}
	private static class SingletonDemo5InnerClass{
		private static final SingletonDemo5 singletonDemo5 = new SingletonDemo5();
	}
}

enum SingletonDemo6 {
    INSTANCE;
    public String syso() {
    	return "23";
    }
}
