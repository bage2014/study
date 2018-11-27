package com.bage.study.java.multhread.officialdemo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 官方demo
 * @author bage
 *
 */
public class PausableThreadPoolExecutor extends ThreadPoolExecutor {
	   private boolean isPaused;
	   private ReentrantLock pauseLock = new ReentrantLock();
	   private Condition unpaused = pauseLock.newCondition();

	   /**
	    * 
	    * @param corePoolSize 核心池的大小
	    * @param maximumPoolSize 线程池最大线程数
	    * @param keepAliveTime 保持时间
	    * @param unit 时间单位
	    * @param workQueue 阻塞队列
	    * @param threadFactory 线程工厂
	    * @param handler 拒绝策略
	    */
	   public PausableThreadPoolExecutor(int corePoolSize,
               int maximumPoolSize,
               long keepAliveTime,
               TimeUnit unit,
               BlockingQueue<Runnable> workQueue,
               ThreadFactory threadFactory,
               RejectedExecutionHandler handler) { 
		   super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
	   }

	   protected void beforeExecute(Thread t, Runnable r) {
	     super.beforeExecute(t, r);
	     pauseLock.lock();
	     try {
	       while (isPaused) unpaused.await();
	     } catch (InterruptedException ie) {
	       t.interrupt();
	     } finally {
	       pauseLock.unlock();
	     }
	   }

	   public void pause() {
	     pauseLock.lock();
	     try {
	       isPaused = true;
	     } finally {
	       pauseLock.unlock();
	     }
	   }

	   public void resume() {
	     pauseLock.lock();
	     try {
	       isPaused = false;
	       unpaused.signalAll();
	     } finally {
	       pauseLock.unlock();
	     }
	   }
}
