package com.bage.study.mybatis.plus;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
public class InitService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void init() {

        jdbcTemplate.execute("DROP TABLE IF EXISTS user;\n" +
                "\n" +
                "CREATE TABLE user\n" +
                "(\n" +
                "\tid BIGINT(20) NOT NULL COMMENT '主键ID',\n" +
                "\tname VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',\n" +
                "\tage INT(11) NULL DEFAULT NULL COMMENT '年龄',\n" +
                "\temail VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',\n" +
                "\tPRIMARY KEY (id)\n" +
                ");" +
                "");


        jdbcTemplate.execute("DELETE FROM user;\n" +
                "\n" +
                "INSERT INTO user (id, name, age, email) VALUES\n" +
                "(1, 'Jone', 18, 'test1@baomidou.com'),\n" +
                "(2, 'Jack', 20, 'test2@baomidou.com'),\n" +
                "(3, 'Tom', 28, 'test3@baomidou.com'),\n" +
                "(4, 'Sandy', 21, 'test4@baomidou.com'),\n" +
                "(5, 'Billie', 24, 'test5@baomidou.com');");
    }

}
