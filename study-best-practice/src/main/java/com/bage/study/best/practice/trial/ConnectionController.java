package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.trial.jdbc.ConnectionUtils;
import com.bage.study.best.practice.trial.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@RequestMapping("/connection")
@RestController
@Slf4j
public class ConnectionController {

    @Autowired
    private MetricService metricService;
    @Autowired
    private RedisService redisService;

    @RequestMapping("/get")
    public Object get(@RequestParam(value = "close", required = false) Boolean close) {
        metricService.increment("get", "ConnectionController");
        long start = System.currentTimeMillis();

        Connection connection = null;
        int result = 0;
        try {
            System.out.println("close0:" + close);
            close = close == null ? true:close;
            System.out.println("close1:" + close);
            connection = ConnectionUtils.getConnection();
            System.out.println("connection:" + connection);
            result = 1;
        } catch (Exception e) {
            result = 2;
            throw new RuntimeException(e);
        } finally {
            if(Boolean.TRUE.equals(close)){
                if(connection != null){
                    try {
                        connection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        long end = System.currentTimeMillis();
        log.info("ConnectionController get cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "get", "ConnectionController");
        return new RestResult(200, result);
    }

}
