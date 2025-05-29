package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.service.UserService;
import com.bage.study.best.practice.trial.mysql.MySQLService;
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
    private MySQLService mySQLService;
    @Autowired
    private UserMockService userMockService;
    @Autowired
    private MetricService metricService;

    @RequestMapping("/sql/slow")
    public Object sqlSlow(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        metricService.increment("sqlSlow", "MySQLController");
        List<User> users = mySQLService.sqlSlow(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController sqlSlow cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"sqlSlow", "MySQLController");
        return users;
    }

    @RequestMapping("/io/high")
    public Object ioHigh(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        metricService.increment("ioHigh", "MySQLController");
        List<User> users = mySQLService.ioHigh(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController ioHigh cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"ioHigh", "MySQLController");
        return users;
    }

    @RequestMapping("/memory/high")
    public Object memoryHigh(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        metricService.increment("memoryHigh", "MySQLController");
        List<User> users = mySQLService.memoryHigh(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController memoryHigh cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"memoryHigh", "MySQLController");
        return users;
    }

    @RequestMapping("/thread/active/high")
    public Object highActiveThread(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        metricService.increment("highActiveThread", "MySQLController");
        List<User> users = mySQLService.highActiveThread(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController highActiveThread cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"highActiveThread", "MySQLController");
        return users;
    }

    @RequestMapping("/query/key")
    public Object queryByKey(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        metricService.increment("queryByKey", "MySQLController");
        List<User> users = mySQLService.queryByKey(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController queryByKey cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"queryByKey", "MySQLController");
        return users;
    }

    @RequestMapping("/query/100")
    public Object query100(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        metricService.increment("query100", "MySQLController");
        List<User> users = mySQLService.query100(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController query100 cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"query100", "MySQLController");
        return users;
    }

    @RequestMapping("/query/10")
    public Object query10(@RequestParam("key") String key) {
        long start = System.currentTimeMillis();
        metricService.increment("query10", "MySQLController");
        List<User> users = mySQLService.query10(key);
        long end = System.currentTimeMillis();
        log.info("MySQLController query10 cost = {}, users = {}", (end - start),users);
        metricService.record((end - start), TimeUnit.MILLISECONDS,"query10", "MySQLController");
        return users;
    }

}
