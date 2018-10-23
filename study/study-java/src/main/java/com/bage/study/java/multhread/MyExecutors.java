package com.bage.study.java.multhread;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executors 的基本使用<br>
 * 都是基于 ThreadPoolExecutor 实现
 * @author bage
 *
 */
public class MyExecutors {

	public static void main(String[] args) {
		
		
		ExecutorService executors = Executors.newCachedThreadPool();
		// 最多同时 n 个线程在执行状态
		
		// 要想运行，注掉下面4行
		int n = 3;
		executors = Executors.newFixedThreadPool(n);
		executors = Executors.newSingleThreadExecutor();
		executors = Executors.newScheduledThreadPool(n);

		
		for (int i = 0; i < 5; i++) {
			if(executors instanceof ScheduledExecutorService){
				((ScheduledExecutorService)executors).scheduleAtFixedRate(
						new MyRunnable(i), 
						1, 3, TimeUnit.SECONDS); // 延迟1秒后每3秒执行一次
			} else {
				executors.execute(new MyRunnable(i));
			}
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

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}