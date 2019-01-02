package com.bage.study.jdbc.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/ustomer")
public class CustomerController {

	 @Autowired
	 JdbcTemplate jdbcTemplate;
	 
	 @GetMapping
	 @ResponseBody
	 public String show() {
		 StringBuilder sb = new StringBuilder();
		 jdbcTemplate.query(
	                "SELECT id, first_name, last_name FROM customers WHERE first_name = ?", new Object[] { "Josh" },
	                (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
	        ).forEach(customer -> sb.append(customer.toString()).append("\n"));
		
		 return sb.toString();
	 }
	
}
