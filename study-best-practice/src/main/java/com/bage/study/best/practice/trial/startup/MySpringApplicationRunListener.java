package com.bage.study.best.practice.trial.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.LocalTime;

@Slf4j
public class MySpringApplicationRunListener implements SpringApplicationRunListener {
    private Long startTime;

    public MySpringApplicationRunListener(SpringApplication application, String[] args) {
    }

    public void starting() {
        startTime = System.currentTimeMillis();
        log.info("MySpringListener启动开始 {}", LocalTime.now());
    }

    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("MySpringListener环境准备 准备耗时：{}毫秒", (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("MySpringListener上下文准备 耗时：{}毫秒", (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("MySpringListener上下文载入 耗时：{}毫秒", (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
    }

    public void finished(ConfigurableApplicationContext context, Throwable exception) {
        log.info("MySpringListener结束 耗时：{}毫秒", (System.currentTimeMillis() - startTime));
        startTime = System.currentTimeMillis();
    }

}
