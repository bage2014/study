package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.trial.mq.UserMQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mq")
@RestController
@Slf4j
public class MQController {
    @Autowired
    private UserMQService service;
    @Autowired
    private UserMockService userMockService;

    @RequestMapping("/send/random")
    public Object sendRandom() {
        User user = userMockService.mockOne();
        int result = service.send(user);
        return "sendRandom:" + result;
    }


}
