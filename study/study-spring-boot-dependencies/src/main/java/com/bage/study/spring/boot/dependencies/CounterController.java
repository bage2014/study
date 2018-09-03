package com.bage.study.spring.boot.dependencies;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounterController {

	private int counter1 = 0;
	private AtomicInteger counter2 = new AtomicInteger();
	
	@RequestMapping("/")
    String counter1(HttpServletRequest request) {
		System.out.println("home当前次数：" + (counter1 ++ ) + ":" + request.getRemoteHost());
		counter2(request);
		return "Hello World!";
    }
	
	
    String counter2(HttpServletRequest request) {
		System.out.println("counter当前次数：" + counter2.incrementAndGet() + ":" + request.getRemoteHost());
        return "Hello World!";
    }
	
	
}
