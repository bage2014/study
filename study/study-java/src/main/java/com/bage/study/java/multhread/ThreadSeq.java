package com.bage.study.java.multhread;

/**
 * 多线程顺序执行实现
 * @author bage
 *
 */
public class ThreadSeq {

	public static final int count = 1;
	
	public static void main(String[] args) throws Exception {
		
		Thread thread1 = new ThreadSeq1();
		
		// 实现方式
		ThreadSeq2 threadSeq2 = new ThreadSeq2(thread1);  
		Thread thread2 = new Thread(threadSeq2);
		
		
		thread2.start();
		thread1.start();
	}
	
}


/**
 * 继承 Thread 类
 * @author bage
 *
 */
class ThreadSeq1 extends Thread{

	@Override
	public void run() {
		for (int i = 0; i < ThreadSeq.count; i++) {
			System.out.println("thread1");
		}
	}
}

/**
 * 实现 Runnable 接口
 * @author bage
 *
 */
class ThreadSeq2 implements Runnable{

	Thread before ;
	
	public ThreadSeq2(Thread thread ) {
		before = thread;
	}
	
	public void run() {
		try {
			for (int i = 0; i < ThreadSeq.count; i++) {
				before.join();  // Waits for this thread to die. 
				System.out.println("thread2");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}


