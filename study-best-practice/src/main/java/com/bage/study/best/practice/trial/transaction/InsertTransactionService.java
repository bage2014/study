package com.bage.study.best.practice.trial.transaction;

import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

@Component
public class InsertTransactionService {


    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private UserService userService;

    public int insertBatchWithTransaction(List<User> userList) {
        transactionTemplate.execute(status -> {
            // Your insert logic 1 here
            System.out.println("Insert operation1 executed");

            // Your insert logic 2 here
            System.out.println("Insert operation2 executed");

            insertBatchWithNoTransaction(userList);
            return null;
        });
        return 1;
    }

    public int insertBatchWithNoTransaction(List<User> userList) {
        // Your insert logic 1 here
        System.out.println("Insert operation1 executed");

        // Your insert logic 2 here
        System.out.println("Insert operation2 executed");

        int result = 0;
        for (User user : userList) {
            result +=userService.insert(user);
        }
        return result;
    }


}
