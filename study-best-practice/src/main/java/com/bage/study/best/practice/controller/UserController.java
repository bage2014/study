package com.bage.study.best.practice.controller;

import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
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
    private MetricService metricService;

    @RequestMapping("/query")
    public Object query(@RequestParam("phone") String phone) {
        long start = System.currentTimeMillis();
        metricService.increment("query", "UserController");
//        log.info("UserController query phone = {}", phone);
        List<User> users = userService.query(phone);
        long end = System.currentTimeMillis();
        log.info("UserController hhh query cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"query", "UserController");
        return users;
    }

    @RequestMapping("/insert")
    public Object insert() {
        try {
            metricService.increment("insert", "UserController");
            long start = System.currentTimeMillis();
            User user = userMockService.mockOne();
//            log.info("UserController insert user = {}", user);
            int insert = userService.insert(user);
//            log.info("UserController insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("UserController insert cost = {}", (end - start));
            metricService.record((end - start), TimeUnit.MILLISECONDS,"insert", "UserController");
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }
    }

    @Async
    @RequestMapping("/insert/async")
    public CompletableFuture<Object> insertAsync() {
        metricService.increment("insertAsync", "UserController");
        try {
            long start = System.currentTimeMillis();
            User user = userMockService.mockOne();
//            log.info("UserController insert async user = {}", user);
            int insert = userService.insert(user);
//            log.info("UserController insert async insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("UserController insert async cost = {}", (end - start));
            metricService.record((end - start), TimeUnit.MILLISECONDS,"insertAsync", "UserController");
            return CompletableFuture.completedFuture(new RestResult(200, insert));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new RestResult(500, e.getMessage()));
        }
    }

    @RequestMapping("/insert/batch")
    public Object insertBatch(@RequestParam(value = "total", required = false, defaultValue = "200") Integer total) {
        metricService.increment("insertBatch", "UserController");
        try {
            long start = System.currentTimeMillis();
            List<User> userList = userMockService.mockBatch(total);
            log.info("UserController insert user = {}", userList);
            int insert = userService.insertBatch(userList);
            log.info("UserController insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("UserController insert cost = {}", (end - start));
            metricService.record((end - start), TimeUnit.MILLISECONDS,"insertBatch", "UserController");
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }

    }

}
