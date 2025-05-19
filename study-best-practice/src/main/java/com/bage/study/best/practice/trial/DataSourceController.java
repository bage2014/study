package com.bage.study.best.practice.trial;

import com.bage.study.best.practice.metrics.MetricService;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.rest.RestResult;
import com.bage.study.best.practice.service.UserService;
import com.bage.study.best.practice.trial.jdbc.ConnectionUtils;
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
@RequestMapping("/data/source")
@RestController
@Slf4j
public class DataSourceController {

    @Autowired
    private UserService userService;
    @Autowired
    private MetricService metricService;

    @RequestMapping("/jdbc/get")
    public Object jdbcGet(@RequestParam(value = "phone") String phone) {
        metricService.increment("jdbcGet", "DataSourceController");
        long start = System.currentTimeMillis();

        Connection connection = null;
        List<User> query = new ArrayList<>();


        int result = 100;
        try {
            connection = ConnectionUtils.getConnection();
            result = 200;

            //2. 执行SQL查询
            String sqlQuery = "SELECT * FROM tb_user WHERE phone = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, phone);
            ResultSet resultSet = preparedStatement.executeQuery();
            //3. 处理查询结果
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setPhone(resultSet.getString("phone"));
                user.setRemark(resultSet.getString("remark"));
                query.add(user);
            }

        } catch (Exception e) {
            result = 500;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        long end = System.currentTimeMillis();
        log.info("DataSourceController jdbcGet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "jdbcGet", "DataSourceController");
        return new RestResult(result, query);
    }


    @RequestMapping("/pool/get")
    public Object poolGet(@RequestParam(value = "phone") String phone) {
        metricService.increment("poolGet", "DataSourceController");
        long start = System.currentTimeMillis();
        List<User> query = new ArrayList<>();
        try {
            query = userService.query(phone);
        } catch (Exception e) {
        }
        long end = System.currentTimeMillis();
        log.info("DataSourceController poolGet cost = {}", (end - start));
        metricService.record((end - start), TimeUnit.MILLISECONDS, "poolGet", "DataSourceController");
        return new RestResult(200, query);
    }


}
