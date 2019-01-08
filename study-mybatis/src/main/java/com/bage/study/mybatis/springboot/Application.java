package com.bage.study.mybatis.springboot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses=Application.class)
@EnableCaching
public class Application implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void run(String... strings) throws Exception {

        log.info("-------------初始化数据库-----------");

        jdbcTemplate.execute("DROP TABLE org_user IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE org_user("
        		+ "id SERIAL, "
                + "name VARCHAR(255),"
                + "sex VARCHAR(8) "
                + ")");

        // Split up the array of whole names into an array of first/last names
        int n = 10;
        List<Object[]> params = new ArrayList<Object[]>(n);
        for(int i = 0;i < n; i++) {
        	params.add(new Object[] {"姓名" + (i + 1), i % 2 + 1});
        }
		// Uses JdbcTemplate's batchUpdate operation to bulk load data
        jdbcTemplate.batchUpdate("INSERT INTO org_user(name,sex) VALUES (?,?)", params );

        log.info("-------------初始化结束------------");
        
    }
}
