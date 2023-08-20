package com.bage.study.spring.boot.h2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @GetMapping("/show")
    @ResponseBody
    public String show() {
        StringBuilder sb = new StringBuilder();
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[]{"Josh"},
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> sb.append(customer.toString()).append("\n"));

        return sb.toString();
    }

    @GetMapping("/query")
    @ResponseBody
    public String query(@RequestParam("id") Long id) {
        StringBuilder sb = new StringBuilder();
        jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE id = ?", new Object[]{id},
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> sb.append(customer.toString()).append("\n"));
        log.info("query result:{}", sb);
        return sb.toString();
    }

    @RequestMapping("/insert")
    @ResponseBody
    public Object insert() {
        Random random = new Random();
        int random1 = random.nextInt(1000000);
        int random2 = random.nextInt(1000000);
        // Split up the array of whole names into an array of first/last names
        Object[] splitUpNames = new Object[]{"John Woo:" + random1, "Jeff Dean:" + random2};
        int result = jdbcTemplate.update("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);
        log.info("insert result:{}", result);
        return result;
    }

}
