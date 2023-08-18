package com.bage.study.springboot.aop.annotation.flow.copy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class FlowCopyBeanConfig {

    @Bean
    public FlowCopyAspect flowCopyAspect() {
        return new FlowCopyAspect();
    }

    @Bean
    public FlowCopyTraceLogic flowCopyTraceLogic() {
        return new FlowCopyTraceLogic();
    }

    @Bean
    public FlowCopyLogic flowCopyLogic() {
        return new FlowCopyLogic();
    }


    @Bean("flowCopyThreadPoolExecutor")
    @Conditional(FlowCopyThreadPoolExecutorCondition.class)
    public ThreadPoolExecutor flowCopyThreadPoolExecutor() {
        return new ThreadPoolExecutor(10, 20,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024),
                new ThreadFactory() {
                    private AtomicInteger atomicInteger = new AtomicInteger(0);

                    @Override
                    public Thread newThread(Runnable r) {
                        if (atomicInteger.get() > 1000000) {
                            atomicInteger.set(0);
                        }
                        Thread thread = new Thread(r);
                        thread.setName("flowCopyThreadPoolExecutor-pool-" + atomicInteger.incrementAndGet());
                        return thread;
                    }
                },
                new ThreadPoolExecutor.AbortPolicy());
    }


}
