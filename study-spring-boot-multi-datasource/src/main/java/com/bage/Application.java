package com.bage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	@Qualifier("jdbcTemplateOne")
	JdbcTemplate jdbcTemplateOne;
	@Autowired
	@Qualifier("jdbcTemplateTwo")
	JdbcTemplate jdbcTemplateTwo;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public void run(String... args) throws Exception {
		System.out.println(jdbcTemplateOne);
		System.out.println(jdbcTemplateTwo);

	}
}