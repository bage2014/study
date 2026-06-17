package com.study.common.threadpool.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "study.thread-pool")
public class ThreadPoolProperties {

    private int corePoolSize = 4;

    private int maxPoolSize = 8;

    private int queueCapacity = 100;

    private String threadNamePrefix = "study-thread-";

    private int keepAliveSeconds = 60;

    private boolean allowCoreThreadTimeOut = false;
}