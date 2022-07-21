package com.bage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");

        jdbcTemplate.execute("DROP TABLE IF EXISTS user;\n" +
                "\n" +
                "CREATE TABLE user\n" +
                "(\n" +
                "    id BIGINT(20) NOT NULL COMMENT '主键ID',\n" +
                "    name VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',\n" +
                "    age INT(11) NULL DEFAULT NULL COMMENT '年龄',\n" +
                "    email VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',\n" +
                "    PRIMARY KEY (id)\n" +
                ");\n");

        jdbcTemplate.execute("DELETE FROM user;\n" +
                "\n" +
                "INSERT INTO user (id, name, age, email) VALUES\n" +
                "(1, 'Jone', 18, 'test1@baomidou.com'),\n" +
                "(2, 'Jack', 20, 'test2@baomidou.com'),\n" +
                "(3, 'Tom', 28, 'test3@baomidou.com'),\n" +
                "(4, 'Sandy', 21, 'test4@baomidou.com'),\n" +
                "(5, 'Billie', 24, 'test5@baomidou.com');\n");

        // Split up the array of whole names into an array of first/last names
        List<Object[]> splitUpNames = Arrays.asList("John Woo", "Jeff Dean", "Josh Bloch", "Josh Long").stream()
                .map(name -> name.split(" "))
                .collect(Collectors.toList());

        // Use a Java 8 stream to print out each tuple of the list
        splitUpNames.forEach(name -> log.info(String.format("Inserting customer record for %s %s", name[0], name[1])));

        // Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);

        log.info("Querying for customer records where first_name = 'Josh':");
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
    }
}