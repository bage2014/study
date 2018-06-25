package com.bage.study.java.multhread;

/**
 * 多线程实现
 * @author bage
 *
 */
public class ThreadCon {

	public static int count = 10;
	
	public int i = 10;


	public static void ThreadSomeThing(String i) {
		getCount(i);
		// getInstance(i);
	}
	

	public static void main(String[] args) throws Exception {
		
		// 实现方式
		thread();			
	}

	private static void thread() throws Exception {
		Thread thread1 = new Thread(){
			public void run() {
				ThreadSomeThing("1");
			}
		};
		Thread thread2 = new Thread(){
			public void run() {
				ThreadSomeThing("2");
			}
		};
		Thread thread3 = new Thread(){
			public void run() {
				ThreadSomeThing("3");
			}
		};
		Thread thread4 = new Thread(){
			public void run() {
				ThreadSomeThing("4");
			}
		};
		Thread thread5 = new Thread(){
			public void run() {
				ThreadSomeThing("5");
			}
		};
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		thread5.start();
		
	}
	
	
	/**
	 * 模拟抽奖
	 * @param i
	 */
	public synchronized static void getCount(String i) {
		if(count > 0){
			System.out.println("thread"+i+":" + count);
			count --;
		}
	}
	
	public static void getInstance(String i) {
		System.out.println("thread"+i+":" + SingleInstanceClass5.getInstance());
	}
	
}

/**
 * 线程安全
 * @author bage
 *
 */
class SingleInstanceClass{
	
	private static SingleInstanceClass SingleInstanceClass = new SingleInstanceClass();
	private SingleInstanceClass(){
		
	}
	public static SingleInstanceClass getInstance(){
		return SingleInstanceClass;
	}
}

/**
 * 线程不安全
 * @author bage
 *
 */
class SingleInstanceClass2{
	
	private static SingleInstanceClass2 SingleInstanceClass;
	private SingleInstanceClass2(){
		
	}
	public static SingleInstanceClass2 getInstance(){
		if(SingleInstanceClass == null){
			SingleInstanceClass = new SingleInstanceClass2();
		}
		return SingleInstanceClass;
	}
}

/**
 * 线程安全
 * @author bage
 *
 */
class SingleInstanceClass3{
	
	private static SingleInstanceClass3 SingleInstanceClass;
	private SingleInstanceClass3(){
		
	}
	public synchronized static SingleInstanceClass3 getInstance(){
		if(SingleInstanceClass == null){
			SingleInstanceClass = new SingleInstanceClass3();
		}
		return SingleInstanceClass;
	}
}

/**
 * 线程安全
 * @author bage
 *
 */
class SingleInstanceClass4{
	
	private static SingleInstanceClass4 SingleInstanceClass;
	private SingleInstanceClass4(){
		
	}
	public static SingleInstanceClass4 getInstance(){
		// 多个线程会卡在这里，但是不重复进行 new SingleInstanceClass4();
		synchronized (SingleInstanceClass4.class) {
			if(SingleInstanceClass == null){
				SingleInstanceClass = new SingleInstanceClass4();
			}
		}
		return SingleInstanceClass;
	}
}

/**
 * 线程不安全
 * @author bage
 *
 */
class SingleInstanceClass5{
	
	private static SingleInstanceClass5 SingleInstanceClass;
	private SingleInstanceClass5(){
		
	}
	public static SingleInstanceClass5 getInstance(){
		if(SingleInstanceClass == null){
			// 多个线程会卡在这里，然后会重复进行 new SingleInstanceClass5();，可以增加如下判断[注释掉的部分]
			synchronized (SingleInstanceClass5.class) {
				// if(SingleInstanceClass == null){
					SingleInstanceClass = new SingleInstanceClass5();
				// }
			}
		}
		return SingleInstanceClass;
	}
}