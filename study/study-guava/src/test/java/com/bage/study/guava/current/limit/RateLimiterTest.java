package com.bage.study.guava.current.limit;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterTest {

	@Test
	public void rateLimiter() {
		
		int threadCount = 10;
		double permitsPerSecond = 2.0; // 
		final RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond); // rate is "2 permits per second"
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		for (int i = 0; i < threadCount; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
					rateLimiter.acquire(); // may wait
					System.out.println("running ..." + new Date().toString());
				}
			});
		}
	}
	
}
