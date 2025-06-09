package com.bage.study.best.practice.controller;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.mq.MQConfig;
import com.bage.study.best.practice.mq.UserMessageSender;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.service.UserService;
import com.bage.study.best.practice.trial.mq.UserMQService;
import com.bage.study.best.practice.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMockService userMockService;
    @Autowired
    private UserMQService userMQService;

    @RequestMapping("/query")
    public Object query(@RequestParam("phone") String phone) {
        long start = System.currentTimeMillis();
//        log.info("UserController query phone = {}", phone);
        List<User> users = userService.query(phone);
        long end = System.currentTimeMillis();
        log.info("UserController hhh query cost = {}, users = {}", (end - start), users);
        return users;
    }

    @RequestMapping("/insert")
    public Object insert() {
        try {
            long start = System.currentTimeMillis();
            User user = userMockService.mockOne();
//            log.info("UserController insert user = {}", user);
            int insert = userService.insert(user);
//            log.info("UserController insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("UserController insert cost = {}", (end - start));
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }
    }

    @Async
    @RequestMapping("/insert/async")
    public CompletableFuture<Object> insertAsync() {
        try {
            long start = System.currentTimeMillis();
            User user = userMockService.mockOne();
//            log.info("UserController insert async user = {}", user);
            int insert = userService.insert(user);
//            log.info("UserController insert async insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("UserController insert async cost = {}", (end - start));
            return CompletableFuture.completedFuture(new RestResult(200, insert));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new RestResult(500, e.getMessage()));
        }
    }


    @RequestMapping("/insert/async/mq")
    public Object insertAsyncMQ() {
        User user = userMockService.mockOne();
        userMQService.send(user);
        return new RestResult(200, "async mq send success");
    }

    @RequestMapping("/insert/batch")
    public Object insertBatch(@RequestParam(value = "total", required = false, defaultValue = "200") Integer total) {
        try {
            long start = System.currentTimeMillis();
            List<User> userList = userMockService.mockBatch(total);
            log.info("UserController insert user = {}", userList);
            int insert = userService.insertBatch(userList);
            log.info("UserController insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("UserController insert cost = {}", (end - start));
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }

    }

}
