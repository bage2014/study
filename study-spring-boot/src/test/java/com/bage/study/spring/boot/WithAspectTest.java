package com.bage.study.spring.boot;

import com.bage.InstanceBuilder;
import com.bage.generator.DefaultValueGeneratorImpl;
import com.bage.study.springboot.async.Starter;
import com.bage.study.springboot.aop.demo.User;
import com.bage.study.springboot.aop.demo.UserService;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class) 
@SpringBootTest(classes = Starter.class)
public class WithAspectTest {

    @Autowired
    UserService userService;

    @Test
    public void insert() {


    InstanceBuilder builder = new InstanceBuilder();
    builder.setCls(User.class);
    builder.setValueGenerator(new DefaultValueGeneratorImpl());
    Object build = builder.build();

    System.out.println(new GsonBuilder().serializeNulls().create().toJson(build));

//        userService.insert(); // 此方式为 异步调用
    }
}