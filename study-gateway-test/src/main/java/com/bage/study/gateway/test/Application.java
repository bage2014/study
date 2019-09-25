package com.bage.study.gateway.test;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public void run(String... args) throws Exception {
        logger.info("... get Users ...");
       
        RestTemplate restTemplate = new RestTemplate();
        testCall(restTemplate);
        
    }

	private void testCall(final RestTemplate restTemplate) {
		int n = 200;
		ExecutorService executorService = Executors.newFixedThreadPool(n);
		for (int i = 0; i < 4000; i++) {
			final int index = i;
			executorService.execute(new Runnable() {
				
				public void run() {
					long bf =  System.currentTimeMillis();
					// String response = 
							restTemplate.getForObject("http://localhost:8080/org?id=" + ( 209797 + new Random().nextInt(10000)), String.class);
			        System.out.println(index + ":" + (System.currentTimeMillis() - bf));
				}
			});
		}
		executorService.shutdown();
	}

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        
    }

}