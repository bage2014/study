package com.bage.study.springboot.config.start;

import com.bage.study.springboot.config.config.HelloWorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Map;


/**
 * https://www.codenong.com/cs106709457/
 */
@SpringBootApplication
public class ConfigOrderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ConfigOrderApplication.class, args);
    }

    @Autowired
    private ApplicationContext context;

    @Override
    public void run(String... args) throws Exception {
        Map<String, HelloWorldService> beansOfType = context.getBeansOfType(HelloWorldService.class);
        System.out.println(beansOfType);
    }
}