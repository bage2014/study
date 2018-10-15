package com.bage.gateway.user.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bage.gateway.user.domain.Customer;
import com.google.common.util.concurrent.RateLimiter;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	

	final double permitsPerSecond = 100; //
	final RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond); 

	private AtomicInteger counter = new AtomicInteger();

	
	@RequestMapping(method=RequestMethod.GET)
	@ResponseBody
	public String getUserById(@RequestParam("id") String id) {
		rateLimiter.acquire();		
		List<Customer> list = jdbcTemplate.query(
		            "SELECT id, first_name, last_name FROM customers WHERE id = ?", new Object[] { id },
		            new RowMapper<Customer>(){
						public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
		                	return new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"));
						}
		            }
		    );
		System.out.println(counter.incrementAndGet() + "--" + list);
		return id;
	}
	
}
