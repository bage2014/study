package com.bage.study.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudySpringBoot3Application {

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		SpringApplication.run(StudySpringBoot3Application.class, args);
		long end = System.currentTimeMillis();
		System.out.println("timeCost=" + (end-start));
	}

}
