package com.bage.study.java.multhread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock 的基本使用
 * @author bage
 *
 */
public class MyReentrantLockImp {

	static int count = 10;
	static ReentrantLock lock = new ReentrantLock();
	static Condition condition= lock.newCondition();
	public static void main(String[] args) throws Exception {
		
		thread();
		
		//System.out.println(lock.tryLock());
	}
	
	public static void getCount(String i) throws Exception {
		try {
			lock.lock();
			//condition.await();
			//System.out.println("getQueueLength:" + lock.getQueueLength());
			//lock.tryLock();
			//System.out.println("isLocked:" + lock.isLocked());
			if(count  > 0){
				System.out.println("thread"+i+":" + count);
				count --;
			}
//			if(count == 9){
//				throw new Exception();
//			}
//			System.out.println("getHoldCount:" + lock.getHoldCount());
//			System.out.println("getQueueLength:" + lock.getQueueLength());
			//condition.signal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(lock.isLocked()){
				lock.unlock();
			}
		}
	}
	
	public static void ThreadSomeThing(String i) {
		try {
			getCount(i);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// getInstance(i);
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
	
}
