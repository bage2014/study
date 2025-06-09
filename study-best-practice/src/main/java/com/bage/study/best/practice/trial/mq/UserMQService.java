package com.bage.study.best.practice.trial.mq;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.mq.UserMessageSender;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.service.UserService;
import com.bage.study.best.practice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserMQService {

    @Autowired
    private UserMockService userMockService;
    @Autowired
    private UserMessageSender sender;
    @Autowired
    private UserService userService;

    public int send(User user) {
        sender.send(user);
        return 1;
    }
    public int receive(User user) {
        int insert = userService.insert(user);
        log.info("UserMQService finish insert = {}", insert);
        return insert;
    }

}
