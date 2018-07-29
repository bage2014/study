package com.bage.study.java.multhread;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executors 的基本使用
 * @author bage
 *
 */
public class MyExecutors {

	public static void main(String[] args) {
		
		int n = 2;
		ExecutorService executors = Executors.newCachedThreadPool();
		// 最多同时 n 个线程在执行状态
		// ExecutorService executors = Executors.newFixedThreadPool(n);
		
		// ExecutorService executors = Executors.newSingleThreadExecutor();
		
		// ExecutorService executors = Executors.newScheduledThreadPool(n);

		
		for (int i = 0; i < 5; i++) {
			executors.execute(new MyRunnable(i));
		}
		executors.shutdown();
		// executors.shutdownNow();

	}
	
}

class MyRunnable implements Runnable{

	private final int id;
	
	public MyRunnable(int id){
		this.id = id;
	}
	
	@Override
	public void run() {
		
		try {
			System.out.println( "thread-" +  id + " started");
			Thread.sleep(new Random().nextInt(3)*1000);
			System.out.println( "thread-" +  id + " stopped");

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}