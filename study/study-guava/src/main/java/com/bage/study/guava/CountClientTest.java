package com.bage.study.guava;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.client.RestTemplate;

public class CountClientTest {

	public static AtomicInteger counter = new AtomicInteger();

	public static void main(String args[]) {

        final RestTemplate restTemplate = new RestTemplate();
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount );
		for (int i = 0; i < threadCount; i++) {
			executorService.execute(new Runnable() {
				@Override
				public void run() {
			        String res = restTemplate.getForObject("http://localhost:8080?id=" + counter.incrementAndGet(), String.class);
			        System.out.println(new Date().toString() + "-response:" + res);
				}
			});
		}
		
	}
	
	
}
