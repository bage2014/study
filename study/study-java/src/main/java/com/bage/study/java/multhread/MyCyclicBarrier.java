package com.bage.study.java.multhread;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 *  CyclicBarrier 最基本用法
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
			System.out.println("run:" + i + "开始准备");
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
