package com.bage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class Application implements CommandLineRunner {

    @Autowired
    TableMapper tableMapper;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public void run(String... args) throws Exception {
        List<Table> tables = tableMapper.queryByPage(null, null);
        System.out.println(tables.size());

        DataSourceContextHolder.set("db1");
        tables = tableMapper.queryByPage("db1", null);
        System.out.println(Arrays.toString(tables.toArray()));


        DataSourceContextHolder.set("db2");
        tables = tableMapper.queryByPage("db2", null);
        System.out.println(Arrays.toString(tables.toArray()));

    }
}