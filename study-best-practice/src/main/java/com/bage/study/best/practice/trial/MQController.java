package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.trial.mq.MQService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/mq")
@RestController
@Slf4j
public class MQController {
    @Autowired
    private MQService service;

    @RequestMapping("/send/random")
    public Object sendRandom() {
        int result = service.send();
        return "sendRandom:" + result;
    }


}
