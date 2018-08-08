package com.bage.study.java.multhread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基本线程池的API实现
 * @author bage
 *
 */
public class MyThreadPoolExecutor {

	public static void main(String[] args) {
		
		int corePoolSize = 2; // 核心线程池大小
		int maximumPoolSize = 5; // 最大线程池大小
		long keepAliveTime = 10; // 线程池中超过corePoolSize数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间
		TimeUnit unit = TimeUnit.SECONDS; // keepAliveTime时间单位
		BlockingQueue<Runnable> workQueue; // 阻塞任务队列
		ThreadFactory threadFactory; // 新建线程工厂
		RejectedExecutionHandler handler; //  当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理
		
		workQueue = new LinkedBlockingDeque<Runnable>();
		threadFactory = new ThreadFactory() {
			private AtomicInteger count = new AtomicInteger(-1);
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setName("创建线程--thread--" + count.incrementAndGet());
				// System.out.println(thread.getName());
				return thread;
			}
		};
		handler = new RejectedExecutionHandler() {
			
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				System.out.println("拒绝线程：workQueue.size():" + executor.getQueue());			
			}
		};
		// ThreadPoolExecutor
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				corePoolSize , 
				maximumPoolSize , 
				keepAliveTime , 
				unit , 
				workQueue , 
				threadFactory , 
				handler );
		for (int i = 0; i < 20; i++) {
			final int index = i;
			threadPoolExecutor.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(10);
						System.out.println("加入线程：thread-" + index );
						System.out.println("当前：workQueue.size():" + workQueue.size());			
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
		try {
			Thread.sleep(10000);
			threadPoolExecutor.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}
				
//		SynchronousQueue,
//		LinkedBlockingDeque,
//		ArrayBlockingQueue;
		
	}
	
}
