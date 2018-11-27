package com.bage.study.java.multhread;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 *  CyclicBarrier 最基本用法<br>
 *  使用场景.实现多个线程共同等待，同时开始执行任务，可重用，多CountDownLatch一些接口。(类似于CountDownLatch，不可重用)<br>
 *  <br>
 *  CyclicBarrier是一个同步工具类，它允许一组线程互相等待，直到到达某个公共屏障点。<br>
 *  与CountDownLatch不同的是该barrier在释放等待线程后可以重用，所以称它为循环（Cyclic）的屏障（Barrier）<br>
 *  <br>
 *  A synchronization aid that allows a set of threads to all wait for each other to reach a common barrier point. <br>
 *  CyclicBarriers are useful in programs involving a fixed sized party of threads that must occasionally wait for each other.<br> 
 *  The barrier is called cyclic because it can be re-used after the waiting threads are released<br>
 * @author bage
 *
 */
public class MyCyclicBarrier {

	public static void main(String[] args) {

		int n = 10;
		CyclicBarrier cyclicBarrier = new CyclicBarrier(n);
		CountDownLatch countDownLatch = new CountDownLatch(n);
		
		for (int i = 0; i < n; i++) {
			new ThreadC(i,cyclicBarrier,countDownLatch).start();
		}
		
		try {
			countDownLatch.await();
			System.out.println("所有程序执行结束。。。");
			//cyclicBarrier.reset();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
	}

}

class ThreadC extends Thread{
	
	final int i;
	final CyclicBarrier cyclicBarrier;
	final CountDownLatch countDownLatch;
	
	public ThreadC(int i, CyclicBarrier cyclicBarrier, CountDownLatch countDownLatch){
		this.i = i;
		this.cyclicBarrier = cyclicBarrier;
		this.countDownLatch = countDownLatch;
	}
	
	@Override
	public void run() {
		try {
			// System.out.println("run:" + i + "开始准备");
			int t = 1000 * new Random().nextInt(10) ;
			Thread.sleep(t);
			System.out.println("run:" + i + "准备就绪，(t="+t+")");

			cyclicBarrier.await();
			System.out.println("run:" + i  + "执行结束");
			countDownLatch.countDown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
