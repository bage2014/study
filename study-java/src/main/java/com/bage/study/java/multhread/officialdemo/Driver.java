package com.bage.study.java.multhread.officialdemo;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch 官方使用demo
 * @author bage
 *
 */
public class Driver { // ...
	public void main(int N) throws InterruptedException {
		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(N);

		for (int i = 0; i < N; ++i) // create and start threads
			new Thread(new Worker(startSignal, doneSignal,i)).start();

		doSomethingElse(); // don't let run yet
		startSignal.countDown(); // let all threads proceed
		doSomethingElse();
		doneSignal.await(); // wait for all to finish
		doSomethingElse();
	}

	private void doSomethingElse() {
		System.out.println("doSomethingElse is work");

	}
}

class Worker implements Runnable {
	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;
	private final int i;

	Worker(CountDownLatch startSignal, CountDownLatch doneSignal,int i) {
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
		this.i = i;
	}

	public void run() {
		try {
			startSignal.await();
			doWork();
			doneSignal.countDown();
		} catch (InterruptedException ex) {
		} // return;
	}

	void doWork() {
		System.out.println("doWork is work:" + i);
	}
}
