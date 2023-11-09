package com.bage.study.springboot.ext;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * https://juejin.cn/post/7271924341734228023
 * 拓展接口
 *
 * todo bage MyApplicationContextInitializer 未生效
 *
 */
@SpringBootApplication
public class ExtOrderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ExtOrderApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("run --------");
    }

}