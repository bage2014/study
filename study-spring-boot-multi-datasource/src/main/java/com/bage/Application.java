package com.bage;

import com.bage.mapper.db1.OauthClientDetailsMapper1;
import com.bage.mapper.db2.OauthClientDetailsMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application implements CommandLineRunner {

	@Autowired
	@Qualifier("jdbcTemplate1")
	JdbcTemplate jdbcTemplate1;
	@Autowired
	@Qualifier("jdbcTemplate2")
	JdbcTemplate jdbcTemplate2;

	@Autowired
	OauthClientDetailsMapper1 oauthClientDetailsMapper1;
	@Autowired
	OauthClientDetailsMapper2 oauthClientDetailsMapper2;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public void run(String... args) throws Exception {
		System.out.println(jdbcTemplate1);
		System.out.println(jdbcTemplate2);

		System.out.println(oauthClientDetailsMapper1.selectByPrimaryKey("sampleClientId"));
		System.out.println(oauthClientDetailsMapper2.selectByPrimaryKey("sampleClientId"));

	}
}