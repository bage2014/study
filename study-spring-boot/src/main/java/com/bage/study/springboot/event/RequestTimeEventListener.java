package com.bage.study.springboot.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.ServletRequestHandledEvent;

@Component
public class RequestTimeEventListener implements ApplicationListener<ServletRequestHandledEvent> {
   Logger log = LoggerFactory.getLogger(RequestTimeEventListener.class);
    @Override
    public void onApplicationEvent(ServletRequestHandledEvent event) {
        Throwable failureCause = event.getFailureCause();
        String failureMessage = failureCause == null ? "" : failureCause.getMessage();
        if (failureCause != null) {
            failureMessage = failureCause.getMessage();
        }
        String clientAddress = event.getClientAddress();
        String requestUrl = event.getRequestUrl();
        String method = event.getMethod();
        long processingTimeMillis = event.getProcessingTimeMillis();
        if (failureMessage != null) {
            log.info("clientAddress = {} requestUrl = {} method = {} costTime = {}",clientAddress,requestUrl,method,processingTimeMillis);
        } else {
            log.error("clientAddress = {} requestUrl = {} method = {} costTime = {} error = {}",clientAddress,requestUrl,method,processingTimeMillis,failureMessage);
        }
    }

}