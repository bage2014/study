
package com.bage.study.spring.boot3.security.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BasicSecurityApplication {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        SpringApplication.run(BasicSecurityApplication.class, args);
        long end = System.currentTimeMillis();
        System.out.println("timeCost=" + (end-start));

        // curl -XPOST user:user@localhost:8080/oauth2/token
        // curl -XPOST user:user@localhost:8080/token
        // curl my-client:my-secret@localhost:8080/token -d grant_type=client_credentials
        // curl my-client:my-secret@localhost:8080/oauth2/token -d grant_type=client_credentials
    }
}