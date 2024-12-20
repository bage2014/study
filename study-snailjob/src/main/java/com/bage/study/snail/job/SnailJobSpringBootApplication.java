package com.bage.study.snail.job;

import com.aizuda.snailjob.client.starter.EnableSnailJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 这里还有另一种写法 @EnableSnailJob("test_sj_group") 
// 注解优先级 > 配置文件优先级 建议直接配置到配置文件中
@EnableSnailJob
public class SnailJobSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SnailJobSpringBootApplication.class, args);
    }
}