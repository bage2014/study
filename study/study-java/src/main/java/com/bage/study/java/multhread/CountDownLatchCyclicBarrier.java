package com.bage.study.java.multhread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 比较两者区别<br>
 * 两者结合起来，可以实现类似于同时开始进行，在所有都结束后进行统计的功能 {@link com.bage.study.java.multhread.MyCyclicBarrier} <br>
 * 线程在countDown()之后，会继续执行自己的任务，而CyclicBarrier会在所有线程任务结束之后，才会进行后续任务<br>
 * 参考链接：https://blog.csdn.net/tolcf/article/details/50925145
 * <br>
 * <br>
 * CountDownLatch : 一个线程(或者多个)， 等待另外N个线程完成某个事情之后才能执行。   CyclicBarrier        : N个线程相互等待，任何一个线程完成之前，所有的线程都必须等待。
 * 对于CountDownLatch来说，重点是那个“一个线程”, 是它在等待， 而另外那N的线程在把“某个事情”做完之后可以继续等待，可以终止。而对于CyclicBarrier来说，重点是那N个线程，他们之间任何一个没有完成，所有的线程都必须等待。<br>
 * 参考链接：https://blog.csdn.net/kjfcpua/article/details/7300286
 * @author bage
 *
 */
public class CountDownLatchCyclicBarrier {

	public static void main(String[] args) {

//		CountDownLatch countDownLatch = new CountDownLatch(5);
//		for (int i = 0; i < 5; i++) {
//			new Thread(new readNum(i, countDownLatch)).start();
//		}
//		try {
//			countDownLatch.await();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println("线程执行结束。。。。");

		CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new Runnable() {
			public void run() {
				System.out.println("线程组执行结束");
			}
		});
		for (int i = 0; i < 5; i++) {
			new Thread(new readNum(i, cyclicBarrier)).start();
		}
		
		// CyclicBarrier 可以重复利用，
		// 这个是CountDownLatch做不到的
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 for (int i = 11; i < 16; i++) {
			 new Thread(new readNum(i,cyclicBarrier)).start();
		 }

	}

	static class readNum implements Runnable {
		private int id;
		private CountDownLatch latch;
		private CyclicBarrier cyc;

		public readNum(int id, CyclicBarrier cyc) {
			this.id = id;
			this.cyc = cyc;
		}

		public readNum(int id, CountDownLatch latch) {
			this.id = id;
			this.latch = latch;
		}

		public void run() {
			synchronized (this) {
				if (latch != null) {
					System.out.println("id:" + id);
					latch.countDown();
					System.out.println("线程组任务" + id + "结束，其他任务继续");
				}
				if (cyc != null) {
					System.out.println("id:" + id);
					try {
						cyc.await();
						System.out.println("线程组任务" + id + "结束，其他任务继续");
					} catch (Exception e) {
						e.printStackTrace();

					}
				}
			}
		}
	}

}
