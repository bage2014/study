package com.bage.study.best.practice;

import com.bage.study.best.practice.service.mock.UserServiceMockImpl;
import com.bage.study.best.practice.repo.UserMapper;
import com.bage.study.best.practice.repo.UserEntity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@MapperScan("com.bage.study.best.practice.repo")
public class BestPracticeApplication implements CommandLineRunner {

    public static void main(String args[]) {
        SpringApplication.run(BestPracticeApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        System.out.println(("----- BestPracticeApplication is started ------"));

    }

}