package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.service.UserMockService;
import com.bage.study.best.practice.trial.transaction.InsertTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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


    @RequestMapping("/insert/batch/transaction")
    public Object insertBatchWithTransaction(@RequestParam(value = "total", required = false, defaultValue = "200") Integer total) {
        try {
            long start = System.currentTimeMillis();
            List<User> userList = userMockService.mockBatch(total);
            log.info("insert user = {}", userList);
            int insert = insertTransactionService.insertBatchWithTransaction(userList);
            log.info("insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("insert cost = {}", (end - start));
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }

    }

    @RequestMapping("/insert/batch/no/transaction")
    public Object insertBatchNoTransaction(@RequestParam(value = "total", required = false, defaultValue = "200") Integer total) {
        try {
            long start = System.currentTimeMillis();
            List<User> userList = userMockService.mockBatch(total);
            log.info("insert user = {}", userList);
            int insert = insertTransactionService.insertBatchWithTransaction(userList);
            log.info("insert insert = {}", insert);
            long end = System.currentTimeMillis();
            log.info("insert cost = {}", (end - start));
            return new RestResult(200, insert);
        } catch (Exception e) {
            return new RestResult(500, e.getMessage());
        }

    }

}
