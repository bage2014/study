package com.bage.study.springboot.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * 参考链接：https://mp.weixin.qq.com/s/cvZe7Ul5QCILMC75jVSY7g
 * @author luruihua
 *
 */
@SpringBootApplication
@EnableAsync
public class Starter {

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncService-");
        executor.initialize();
        return executor;
    }

}