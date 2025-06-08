package com.bage.study.best.practice.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MQMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MQMessageReceiver receiver;

    public void send(String key, String body) {
        log.info("MQMessageSender send key = {}, body = {}", key, body);
        rabbitTemplate.convertAndSend(MQConfig.topicExchangeName, key, body);
    }
}