package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.metrics.MetricService;
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

/**
 * 参考 com.bage.study.best.practice.controller.MySQLController
 */
@RequestMapping("/mysql")
@RestController
@Slf4j
public class MySQLController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMockService userMockService;
    @Autowired
    private MetricService metricService;

    @RequestMapping("/query/key")
    public Object queryByKey(@RequestParam("phone") String phone) {
        long start = System.currentTimeMillis();
        metricService.increment("queryByKey", "MySQLController");
        List<User> users = userService.query(phone);
        long end = System.currentTimeMillis();
        log.info("MySQLController queryByKey cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"queryByKey", "MySQLController");
        return users;
    }

    @RequestMapping("/query/100")
    public Object query100(@RequestParam("phone") String phone) {
        long start = System.currentTimeMillis();
        metricService.increment("query100", "MySQLController");
        List<User> users = userService.query(phone);
        long end = System.currentTimeMillis();
        log.info("MySQLController query100 cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"query100", "MySQLController");
        return users;
    }

    @RequestMapping("/query/10")
    public Object query10(@RequestParam("phone") String phone) {
        long start = System.currentTimeMillis();
        metricService.increment("query10", "MySQLController");
        List<User> users = userService.query(phone);
        long end = System.currentTimeMillis();
        log.info("MySQLController query10 cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"query10", "MySQLController");
        return users;
    }

    @RequestMapping("/insert/one")
    public Object insertOne() {
        try {
            metricService.increment("insertOne", "MySQLController");
            long start = System.currentTimeMillis();
            User user = userMockService.mockOne();
            int insert = userService.insert(user);
            long end = System.currentTimeMillis();
            log.info("MySQLController insertOne cost = {}", (end - start));
            metricService.record((end - start), TimeUnit.MILLISECONDS,"insertOne", "MySQLController");
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }
    }

    @RequestMapping("/insert/batch")
    public Object insertBatch(@RequestParam(value = "total", required = false, defaultValue = "200") Integer total) {
        metricService.increment("insertBatch", "MySQLController");
        try {
            long start = System.currentTimeMillis();
            List<User> userList = userMockService.mockBatch(total);
            log.info("MySQLController insert user = {}", userList);
            int insert = userService.insertBatch(userList);
            log.info("MySQLController insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("MySQLController insert cost = {}", (end - start));
            metricService.record((end - start), TimeUnit.MILLISECONDS,"insertBatch", "MySQLController");
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }
    }



}
