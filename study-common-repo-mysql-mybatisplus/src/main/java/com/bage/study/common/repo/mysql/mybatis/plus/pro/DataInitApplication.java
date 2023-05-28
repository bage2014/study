package com.bage.study.common.repo.mysql.mybatis.plus.pro;

import com.bage.study.common.repo.mysql.mybatis.plus.pro.dao.UserMapper;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.model.UserPro;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.service.UserProDataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
public class DataInitApplication implements CommandLineRunner {

    @Resource
    private UserProDataService userProDataService;
    @Resource
    private UserMapper userMapper;

    public static void main(String args[]) {
        SpringApplication.run(DataInitApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        List<UserPro> initData = userProDataService.getInitData();
        for (UserPro initDatum : initData) {

        }
    }
}