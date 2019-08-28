package com.bage.study.spring.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.qq.com");
//        mailSender.setPort(587);

        mailSender.setUsername("893542907@qq.com");
        mailSender.setPassword("");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
//
//    @Bean
//    public JavaMailSenderImpl javaMailSender(){
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost("smtp.qq.com");
//        return javaMailSender;
//    }
//
//    @Bean
//    public SimpleMailMessage simpleMailMessage(){
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        simpleMailMessage.setFrom("833542907@qq.com");
//        simpleMailMessage.setSubject("10086");
//        return simpleMailMessage;
//    }

    @Autowired
    OrderManager orderManager;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public void run(String... args) throws Exception {
        orderManager.placeOrder(null);

    }
}