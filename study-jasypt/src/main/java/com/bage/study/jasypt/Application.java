package com.bage.study.jasypt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        System.setProperty("jasypt.encryptor.password", "password");
        AppConfigForJasyptSimple service = applicationContext
                .getBean(AppConfigForJasyptSimple.class);

        System.out.println("Password@1:" + service.getProperty());

        Environment environment = applicationContext.getBean(Environment.class);

        System.out.println("Password@1:" + service.getPasswordUsingEnvironment(environment));
    }
}
