package com.bage.study.gc.jdk17;

import com.bage.study.gc.biz.GcSafePointService;
import com.bage.study.gc.biz.GoodsSecKillService;
import com.bage.study.gc.biz.gc.JvmGcService;
import com.bage.study.gc.biz.cpu.high.HighCpuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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


    @Bean
    public HighCpuService DemoService(){
        return new HighCpuService();
    }

}