package com.bage.study.springboot.inject.circur;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 *
 * ref:  https://mp.weixin.qq.com/s/Un8pyET2XDXpDY4FnRbwXw
 *
 * 
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Resource
    AService aService;
    @Resource
    BService bService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(aService);
        System.out.println(bService);
    }

}