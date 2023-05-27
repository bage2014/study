package com.bage.study.common.repo.mysql.mybatis.plus.pro;

import com.bage.study.common.repo.mysql.mybatis.plus.beta.User;
import com.bage.study.common.repo.mysql.mybatis.plus.beta.UserMapper;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.model.UserPro;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.service.UserProDataService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
public class DataInitApplication implements CommandLineRunner {

    @Resource
    private UserProDataService userProDataService;

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