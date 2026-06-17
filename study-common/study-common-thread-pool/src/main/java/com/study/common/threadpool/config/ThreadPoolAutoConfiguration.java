package com.study.common.threadpool.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Slf4j
@Configuration
@EnableConfigurationProperties(ThreadPoolProperties.class)
@RequiredArgsConstructor
public class ThreadPoolAutoConfiguration {

    private final ThreadPoolProperties properties;

    @Bean
    public ExecutorService studyThreadPool() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int counter = 0;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, properties.getThreadNamePrefix() + counter++);
                thread.setDaemon(false);
                return thread;
            }
        };

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getQueueCapacity()),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        executor.allowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut());

        log.info("Study ThreadPool initialized: corePoolSize={}, maxPoolSize={}, queueCapacity={}",
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getQueueCapacity());

        return executor;
    }

    @Bean
    public ScheduledExecutorService studyScheduledThreadPool() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int counter = 0;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, properties.getThreadNamePrefix() + "scheduled-" + counter++);
                thread.setDaemon(false);
                return thread;
            }
        };

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
                properties.getCorePoolSize(),
                threadFactory,
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        executor.setKeepAliveTime(properties.getKeepAliveSeconds(), TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(properties.isAllowCoreThreadTimeOut());

        log.info("Study ScheduledThreadPool initialized: corePoolSize={}", properties.getCorePoolSize());

        return executor;
    }
}