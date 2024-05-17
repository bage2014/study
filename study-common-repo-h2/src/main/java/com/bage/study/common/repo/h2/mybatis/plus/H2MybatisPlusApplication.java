package com.bage.study.common.repo.h2.mybatis.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@MapperScan("com.bage.study.common.repo.h2.mybatis.plus")
public class H2MybatisPlusApplication implements CommandLineRunner {

    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private UserMapper userMapper;

    public static void main(String args[]) {
        SpringApplication.run(H2MybatisPlusApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        System.out.println(("----- selectAll size ------" + userList.size()));
        for (User user : userList) {
            System.out.println(user);
        }
    }
}