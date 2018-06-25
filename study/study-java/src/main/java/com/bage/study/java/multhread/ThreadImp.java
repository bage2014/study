package com.bage.study.java.multhread;

/**
 * 多线程实现
 * @author bage
 *
 */
public class ThreadImp {

	public static final int count = 10;
	
	public static void main(String[] args) throws Exception {
		
		// 实现方式
		thread1();
		thread2();
		
		// 线程的状态
//		NEW
//		 A thread that has not yet started is in this state. 
//		•RUNNABLE
//		 A thread executing in the Java virtual machine is in this state. 
//		•BLOCKED
//		 A thread that is blocked waiting for a monitor lock is in this state. 
//		•WAITING
//		 A thread that is waiting indefinitely for another thread to perform a particular action is in this state. 
//		•TIMED_WAITING
//		 A thread that is waiting for another thread to perform an action for up to a specified waiting time is in this state. 
//		•TERMINATED
//		 A thread that has exited is in this state.

		
	}

	private static void thread2() {
		Thread2 myThread = new Thread2();  
		Thread thread = new Thread(myThread);
		thread.start();
	}

	private static void thread1() throws Exception {
		Thread thread = new Thread1();
		System.out.println(thread.getState());
		thread.start();
		

		
	}
	
}


/**
 * 继承 Thread 类
 * @author bage
 *
 */
class Thread1 extends Thread{

	@Override
	public void run() {
		for (int i = 0; i < ThreadImp.count; i++) {
			System.out.println("thread1" + i);
			try {
				Thread.sleep(1000);
				System.out.println(getState());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(getState());
			System.out.println("thread1" + i);
		}
	}
}

/**
 * 实现 Runnable 接口
 * @author bage
 *
 */
class Thread2 implements Runnable{

	public void run() {
		for (int i = 0; i < ThreadImp.count; i++) {
			System.out.println("thread2" + i);
		}
	}
}


