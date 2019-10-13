package com.bage.study.spring.boot.oauth2.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@SpringBootApplication
//@EnableAuthorizationServer
//@EnableResourceServer
//@RestController
public class JwtAuthServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(JwtAuthServerApplication.class, args);
	}
	@RequestMapping(value = "/products")
	public String getProductName() {
		return "Honey";
	}
}