package com.bage.study.springboot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class SpringUtilsApplication implements CommandLineRunner {

    @Autowired
    private ConfigurableApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(SpringUtilsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        SpringUtils.getPid();
        SpringUtils.getMainDir();
        SpringUtils.getJavaVersion();
        SpringUtils.getYmlFile();
        SpringUtils.getBasePackages(context);

    }

}