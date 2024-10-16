package com.bage.study.spring.boot.oauth2.server.v3;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AuthorizationServerApplication implements CommandLineRunner {
    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }


    @Override
    public void run(String... args) {
//        creditAuth();

        passwordAuth();

    }

    private void creditAuth() {
        String url = "http://localhost:8080/oauth/token";
        HttpHeaders header = new HttpHeaders();
        String userAndPass = "client:secret";
        header.add("Authorization", "Basic " + Base64.encodeBase64String(userAndPass.getBytes()));

        System.out.println("Authorization:" + header.get("Authorization"));


        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "client_credentials");

        HttpEntity<Object> entity = new HttpEntity<Object>(map, header);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String sttr = response.getBody();

            System.out.println("return:" + sttr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void passwordAuth() {
        String url = "http://localhost:8080/oauth/token";
        HttpHeaders header = new HttpHeaders();
        String userAndPass = "client:secret";
        header.add("Authorization", "Basic " + Base64.encodeBase64String(userAndPass.getBytes()));

        System.out.println("Authorization:" + header.get("Authorization"));


        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", "bage");
        map.add("password", "bage");
        map.add("grant_type", "password");

        HttpEntity<Object> entity = new HttpEntity<Object>(map, header);

        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
            String sttr = response.getBody();

            System.out.println("return:" + sttr);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}