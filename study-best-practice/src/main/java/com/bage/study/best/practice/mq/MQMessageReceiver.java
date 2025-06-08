package com.bage.study.best.practice.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQMessageReceiver {

    public void receiveMessage(String message) {
        log.info("MQMessageReceiver Received <" + message + ">");
    }

}