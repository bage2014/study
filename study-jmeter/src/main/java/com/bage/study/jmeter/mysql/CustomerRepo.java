package com.bage.study.jmeter.mysql;

import com.bage.study.jmeter.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomerRepo {
    private static final Logger log = LoggerFactory.getLogger(CustomerRepo.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

//    @PostConstruct
    public void init() {

        log.info("Creating tables");

//        jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
//        jdbcTemplate.execute("CREATE TABLE customers(" +
//                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

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
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[]{"Josh"},
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        ).forEach(customer -> log.info(customer.toString()));
    }

    public List<Customer> query(String key) {
        return jdbcTemplate.query(
                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[]{key},
                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        );
    }

    public int insert(List<Customer> list) {
        if(Objects.isNull(list)){
            return 0;
        }
        List<Object[]> splitUpNames = new ArrayList<>();

        for (Customer customer : list) {
            Object[] objs = new Object[2];
            objs[0] = customer.getFirstName();
            objs[1] = customer.getLastName();
            splitUpNames.add(objs);
        }
        int[] res = jdbcTemplate.batchUpdate("INSERT INTO customers(first_name, last_name) VALUES (?,?)", splitUpNames);
        return Arrays.stream(res).sum();
    }
}