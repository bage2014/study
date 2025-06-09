package com.bage.study.best.practice.mq;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMessageSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(User user) {
        String key = MQConfig.queueKey;
        String body = JsonUtils.toJson(user);
        log.info("MQMessageSender send key = {}, body = {}", key, body);
        rabbitTemplate.convertAndSend(MQConfig.topicExchangeName, key, body);
    }
}