package com.bage.study.java.multhread;


/**
 * 顺序执行 thread.join();
 * Waits for this thread to die. 
 * @author bage
 *
 */
public class Seq {

	static Thread1 Thread1 = new Thread1();
	static Thread2 Thread2 = new Thread2(Thread1);
	static Thread3 Thread3 = new Thread3(Thread2);
	
	public static void main(String[] args) {
		Thread1.start();
		Thread2.start();
		Thread3.start();
	}
	
	static class Thread1 extends Thread{
		@Override
		public void run() {
			System.out.println("Thread1 is work");
		}
	}
	static class Thread2 extends Thread{
		Thread thread;
		public Thread2(Thread thread) {
			this.thread = thread;
		}

		@Override
		public void run() {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Thread2 is work");
		}
	}
	static class Thread3 extends Thread{
		Thread thread;
		public Thread3(Thread thread) {
			this.thread = thread;
		}


		@Override
		public void run() {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Thread3 is work");
		}
	}
}
