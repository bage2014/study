package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.service.UserService;
import com.bage.study.best.practice.trial.jdbc.ConnectionUtils;
import com.bage.study.best.practice.trial.transaction.InsertTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@RequestMapping("/data/transaction")
@RestController
@Slf4j
public class TransactionController {

    @Autowired
    private UserMockService userMockService;
    @Autowired
    private InsertTransactionService insertTransactionService;
    @Autowired
    private MetricService metricService;


    @RequestMapping("/insert/batch/transaction")
    public Object insertBatchWithTransaction(@RequestParam(value = "total", required = false, defaultValue = "200") Integer total) {
        metricService.increment("insertBatch", "UserController");
        try {
            long start = System.currentTimeMillis();
            List<User> userList = userMockService.mockBatch(total);
            log.info("insert user = {}", userList);
            int insert = insertTransactionService.insertBatchWithTransaction(userList);
            log.info("insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("insert cost = {}", (end - start));
            metricService.record((end - start), TimeUnit.MILLISECONDS,"insertBatch", "UserController");
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }

    }

    @RequestMapping("/insert/batch/no/transaction")
    public Object insertBatchNoTransaction(@RequestParam(value = "total", required = false, defaultValue = "200") Integer total) {
        metricService.increment("insertBatch", "UserController");
        try {
            long start = System.currentTimeMillis();
            List<User> userList = userMockService.mockBatch(total);
            log.info("insert user = {}", userList);
            int insert = insertTransactionService.insertBatchWithTransaction(userList);
            log.info("insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("insert cost = {}", (end - start));
            metricService.record((end - start), TimeUnit.MILLISECONDS,"insertBatch", "UserController");
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }

    }

}
