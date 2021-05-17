package com.bage.study.springboot.event;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * ref: https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#context-functionality-events
 *
 * 
 */
@SpringBootApplication
@RestController
public class Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Resource
    private MyEventPublisher publisher;

    @Override
    public void run(String... args) throws Exception {
        MyEvent event = new MyEvent("hello");
        event.setMsg("world");
        publisher.publish(event);
    }

    // 异步化配置，所有的event都是异步执行
    @Bean
    public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor() {
        return new SimpleAsyncTaskExecutor();
    }

    @Bean
    public SimpleApplicationEventMulticaster multicaster() {
        SimpleApplicationEventMulticaster simpleApplicationEventMulticaster = new SimpleApplicationEventMulticaster();
        simpleApplicationEventMulticaster.setTaskExecutor(simpleAsyncTaskExecutor());
        return simpleApplicationEventMulticaster;
    }

}