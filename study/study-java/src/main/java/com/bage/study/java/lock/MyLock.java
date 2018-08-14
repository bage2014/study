package com.bage.study.java.lock;

/**
 * 模拟死锁
 * @author bage
 *
 */
public class MyLock {

	public static void main(String[] args) {
		lockTest();
		
		test();

	}

	private static void test() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					System.out.println("1执行中。。。");
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					System.out.println("2执行中。。。");
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
	}

	private static void lockTest() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// 先持有B 锁
				synchronized (B.class) {
					try {
						
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 1000ms后进行分尝试锁 A class
					synchronized (A.class) {
						
					}
				}
			}
		}).start();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// 先持有A锁
				synchronized (A.class) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 1000ms后进行分尝试锁 B class
					synchronized (B.class) {
						
					}
				}
				
			}
		}).start();
	}
	
}
class A {
	
}
 
class B {
	

	
}
