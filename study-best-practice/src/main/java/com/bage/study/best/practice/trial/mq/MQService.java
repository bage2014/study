package com.bage.study.best.practice.trial.mq;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.mq.MQConfig;
import com.bage.study.best.practice.mq.MQMessageSender;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MQService {

    @Autowired
    private UserMockService userMockService;
    @Autowired
    private MQMessageSender sender;

    public int send() {
        User user = userMockService.mockOne();
        sender.send(MQConfig.queueKey, JsonUtils.toJson(user));
        return 0;
    }

}
