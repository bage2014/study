package com.bage.study.best.practice.mq;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.trial.mq.UserMQService;
import com.bage.study.best.practice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserMessageReceiver {

    @Autowired
    private UserMQService userMQService;


    public void receiveMessage(String message) {
        log.info("MQMessageReceiver receiveMessage message = {}", message);
        User user = JsonUtils.fromJson(message, User.class);
        userMQService.receive(user);
    }

}