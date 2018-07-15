package com.bage.study.java.multhread;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 的使用
 * @author bage
 *
 */
public class MyCountDownLatch {

	public static void main(String[] args) throws InterruptedException {

		// defaultExample();
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
			new Driver().main(1000);
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
