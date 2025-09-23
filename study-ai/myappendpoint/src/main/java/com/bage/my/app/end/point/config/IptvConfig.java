package com.bage.my.app.end.point.config;

import com.bage.my.app.end.point.service.IptvService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class IptvConfig {

    @Bean
    public CommandLineRunner loadIptvData(IptvService iptvService) {
        return args -> {
            log.info("应用启动，开始加载IPTV数据...");
            iptvService.loadIptvData();
            log.info("IPTV数据加载完成");
        };
    }
}