package com.bage.zhongxinyun.test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class MulThreadExecutors{

	boolean isDebug;
	CountDownLatch countDownLatch;
	CyclicBarrier cyclicBarrier;
	List<Runnable> runnables;
	
	public MulThreadExecutors() {
		this(false,null);
	}
	
	public MulThreadExecutors(ExecutorListener executorListener) {
		this(false,executorListener);
	}
	
	public MulThreadExecutors(boolean isDebug,ExecutorListener executorListener) {
		
	}

	public void add(Runnable command) {
		runnables.add(command);
	}
	public void execute() {
		countDownLatch = new CountDownLatch(1);
		for (int i = 0; i < runnables.size(); i++) {
			
		}
		
	}

}
