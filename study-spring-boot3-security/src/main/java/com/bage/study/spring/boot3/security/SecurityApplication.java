
package com.bage.study.spring.boot3.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(SecurityApplication.class, args);
        long end = System.currentTimeMillis();
        System.out.println("timeCost=" + (end-start));
    }
}