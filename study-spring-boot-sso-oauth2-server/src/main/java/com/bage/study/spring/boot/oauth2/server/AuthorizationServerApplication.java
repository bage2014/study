package com.bage.study.spring.boot.oauth2.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
public class AuthorizationServerApplication implements CommandLineRunner {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        // System.out.println(passwordEncoder().encode("secret"));
    }
}