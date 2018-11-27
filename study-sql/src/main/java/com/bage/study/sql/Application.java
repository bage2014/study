package com.bage.study.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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

    	// int n = 10000; // jdbcTemplate-13 s
    	// int n = 100000; // jdbcTemplate-170 s
    	// int n = 100000; // jdbc-148 s
    	int n = 1000000; // jdbcTemplate- s
    	long bf = System.currentTimeMillis();
    	List<Object[]> splitUpNames = initData(n);
		
    	String sql = "INSERT INTO customers(first_name, last_name) VALUES (?,?)";
    	// 直接插入
    	// jdbcTemplate.batchUpdate(sql, splitUpNames);
        
    	// 分批次插入
    	Connection conn = null;
    	try {
    		conn = jdbcTemplate.getDataSource().getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pst = conn.prepareStatement(sql);
			for (int i = 0; i < splitUpNames.size(); i++) {
				pst.setObject(1,splitUpNames.get(i)[0]);   
				pst.setObject(2, splitUpNames.get(i)[1]);   
				pst.addBatch();   
			}
			pst.executeLargeBatch();
			conn.commit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			conn.close();
		}
    	
    	System.out.println((System.currentTimeMillis() - bf) / 1000);
    }
    private  List<Object[]> initData(int n) {
    	List<Object[]> params = new LinkedList<Object[]>();
    	for (int j = 0; j < n; j++) {
    		params.add(new Object[]{"firstName-" + j ,"lastName-" + j});
		}
    	return params;
	}

	public void init() throws Exception {

        log.info("Creating tables");

 		jdbcTemplate.execute("DROP TABLE customers IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE customers(" +
                "id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))");

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