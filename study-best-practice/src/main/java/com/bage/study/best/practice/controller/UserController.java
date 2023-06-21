package com.bage.study.best.practice.controller;

import com.bage.study.best.practice.metrics.CounterMetrics;
import com.bage.study.best.practice.metrics.TimerMetrics;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMockService userMockService;
    @Autowired
    private CounterMetrics counterMetrics;
    @Autowired
    private TimerMetrics timerMetrics;

    @RequestMapping("/query")
    public Object query(@RequestParam("phone") String phone) {
        log.info("UserController query phone = {}", phone);
        List<User> users = userService.query(phone);
        log.info("UserController query users = {}", users);
        return users;
    }

    @RequestMapping("/insert")
    public Object insert() {
        counterMetrics.increment();
        try {
            long start = System.currentTimeMillis();
            User user = userMockService.mockOne();
            log.debug("UserController insert user = {}", user);
            int insert = userService.insert(user);
            log.info("UserController insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("UserController insert cost = {}", (end - start));
            timerMetrics.record((end - start), TimeUnit.MILLISECONDS);
            return new RestResult(200,insert);
        }catch (Exception e){
            return new RestResult(500,e.getMessage());
        }

    }

}
