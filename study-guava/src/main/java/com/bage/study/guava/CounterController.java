package com.bage.study.guava;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.util.concurrent.RateLimiter;

@RestController
public class CounterController {

	private AtomicInteger counter = new AtomicInteger();

	final double permitsPerSecond = 10.0; //
	final RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond); 

	@RequestMapping("/")
	String counter1(@RequestParam("id") String id, HttpServletRequest request) {
		try {
			rateLimiter.acquire();
			Thread.sleep(1000);
			System.out.println((counter.incrementAndGet()) + "id：" + ":" + id);
			return "res-id:" + id;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}

}
