package com.bage.study.spring.boot.starter;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * 链接：https://juejin.cn/post/7479452347714945087
 */
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(MyApplication.class)
               .profiles("dev")
               .properties("server.port=8081")
                // .other config
               .run(args);
    }
}
