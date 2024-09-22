package com.bage.study.spring.boot.oauth2.server;

import org.apache.commons.codec.binary.Base64;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
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
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        // System.out.println(passwordEncoder().encode("secret"));

            String url = "http://localhost:8080/oauth/token";
            HttpHeaders header = new HttpHeaders();
            String userAndPass = "sampleClientId:secret";
            header.add("Authorization", "Basic "+ Base64.encodeBase64String(userAndPass.getBytes()));

            System.out.println("Authorization:" + header.get("Authorization"));


            MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
//            map.add("username", "bage");
//            map.add("password", "bage");

            HttpEntity<Object> entity = new HttpEntity<Object>(map,header);

            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                String str = response.getBody();

                System.out.println("return:" + str);

            } catch (Exception e){
                e.printStackTrace();
            }
    }
}