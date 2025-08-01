package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.trial.jdbc.ConnectionUtils;
import com.bage.study.best.practice.trial.redis.BasicRedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@RequestMapping("/connection")
@RestController
@Slf4j
public class ConnectionController {

    @Autowired
    private BasicRedisService redisService;

    @RequestMapping("/get")
    public Object get(@RequestParam(value = "close", required = false) Boolean close) {
        long start = System.currentTimeMillis();

        Connection connection = null;
        int result = 0;
        try {
            System.out.println("ConnectionController close0:" + close);
            close = close == null ? true:close;
            System.out.println("ConnectionController close1:" + close);
            connection = ConnectionUtils.getConnection();
            System.out.println("ConnectionController connection:" + connection);
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
        return new RestResult(200, result);
    }

    @RequestMapping("/getAndExc")
    public Object getAndExc(@RequestParam(value = "close", required = false) Boolean close,
                            @RequestParam(value = "phone", required = false) String phone) {
        long start = System.currentTimeMillis();

        Connection connection = null;
        int result = 0;
        try {
            System.out.println("ConnectionController close0:" + close);
            close = close == null ? true:close;
            System.out.println("ConnectionController close1:" + close);
            connection = ConnectionUtils.getConnection();
            System.out.println("ConnectionController connection:" + connection);
            result = 1;

            //2. 执行SQL查询
            String sqlQuery = "SELECT * FROM tb_user WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, phone);
            ResultSet resultSet = preparedStatement.executeQuery();
            //3. 处理查询结果
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                System.out.println("id: " + id + ", firstName: " + firstName);
            }

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
        return new RestResult(200, result);
    }

}
