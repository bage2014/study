package com.bage.study.gc.jdk8;

import com.bage.study.gc.biz.GcSafePointService;
import com.bage.study.gc.biz.GoodsSecKillService;
import com.bage.study.gc.biz.JvmGcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@Slf4j
public class BeanConfig {

    @Bean
    public GcSafePointService GcSafePointService(){
        return new GcSafePointService();
    }

    @Bean
    public GoodsSecKillService GoodsSecKillService(){
        return new GoodsSecKillService();
    }

    @Bean
    public JvmGcService JvmGcService(){
        return new JvmGcService();
    }

}