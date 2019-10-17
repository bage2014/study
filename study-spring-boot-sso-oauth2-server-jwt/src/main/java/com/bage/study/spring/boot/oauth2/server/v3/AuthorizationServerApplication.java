package com.bage.study.spring.boot.oauth2.server.v3;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthorizationServerApplication implements CommandLineRunner {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new BCryptPasswordEncoder();
    }
    public static void main(String[] args) throws Throwable {
        SpringApplication.run(AuthorizationServerApplication.class, args);
        System.out.println(new AuthorizationServerApplication().passwordEncoder().encode("secret"));
    }


    @Override
    public void run(String... args) throws Exception {
        // System.out.println(passwordEncoder().encode("secret"));
    }
}