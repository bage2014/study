package com.bage.study.java.multhread;

import java.util.concurrent.CountDownLatch;

import com.bage.study.java.multhread.officialdemo.Driver;

/**
 *  CountDownLatch 的使用<br>
 *  重点：在所有线程都执行结束后，CountDown到0，会唤醒之前await的线程 *  
 *  <br>
 *  CountDownLatch是一个同步工具类，用来协调多个线程之间的同步，或者说起到线程之间的通信（而不是用作互斥的作用）。<br>
 *  CountDownLatch能够使一个线程在等待另外一些线程完成各自工作之后，再继续执行。<br>
 *  使用一个计数器进行实现。计数器初始值为线程的数量。当每一个线程完成自己任务后，计数器的值就会减一。<br>
 *  当计数器的值为0时，表示所有的线程都已经完成了任务，然后在CountDownLatch上等待的线程就可以恢复执行任务。<br>
 *  <br>
 *  场景1.某线程需要等待多个线程执行完毕，再执行。<br>
 *  场景2.实现多个线程共同等待，同时开始执行任务，不可重用。(此类似于CyclicBarrier，可重用)<br>
 * @author bage
 *
 */
public class MyCountDownLatch {

	public static void main(String[] args) throws InterruptedException {

		// 官方demo
		defaultExample();
		
		// 网上例子
		long before = System.currentTimeMillis();
		int n = 400;
		CountDownLatch countDownLatch = new CountDownLatch(n);
		CountDownLatch startLatch = null;//new CountDownLatch(1);

		for (int i = 0; i < n; i++) {
			new thread11(countDownLatch,i,startLatch){}.start();
		}
		
		System.out.println("所有线程执行开始:" + (System.currentTimeMillis() - before));
		before = System.currentTimeMillis();
		//startLatch.countDown();
		countDownLatch.await();
		System.out.println("所有线程执行结束：" + (System.currentTimeMillis() - before));

	}

	private static void defaultExample() {
		try {
			new Driver().main(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}

class thread11 extends Thread {
	private CountDownLatch countDownLatch;
	private CountDownLatch startLatch;
	private int i;

	public thread11(CountDownLatch countDownLatch, int i, CountDownLatch startLatch){
		this.countDownLatch = countDownLatch;
		this.i= i;
		this.startLatch = startLatch;
	}
	
	public void run() {
		try {
			if(startLatch != null){
				startLatch.await();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("线程"+i+"启动...");
		countDownLatch.countDown();
		
	}
};
