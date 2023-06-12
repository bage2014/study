package com.bage.study.mybatis.plus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@MapperScan("com.bage.study.mybatis.plus")
public class MybatisPlusApplication implements CommandLineRunner {

    @Resource
    private InitService initService;
    @Resource
    private CrudService crudService;
    Random random = new Random();

    public static void main(String args[]) {
        SpringApplication.run(MybatisPlusApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(("----- init start ------"));
        initService.init();
        System.out.println(("----- init end ------"));

        int nextInt = random.nextInt(100);
        User user = new User();
        user.setId(System.currentTimeMillis());
        user.setAge(nextInt);
        user.setEmail(random.nextInt(100000) + "@qq.com");
        user.setName("bage" + nextInt);
        System.out.println(("----- insert start ------" + user));
        int insert = crudService.insert(user);
        System.out.println(("----- insert end ------" + insert));

        String name = "bage" + nextInt;
        System.out.println(("----- query start ------" + name));
        List<User> query = crudService.query(name);
        System.out.println(("----- query end ------" + query));

        user.setEmail(user.getEmail() + "-update@.cpm");
        System.out.println(("----- update start ------" + user));
        int update = crudService.update(user);
        System.out.println(("----- update end ------" + update));

        System.out.println(("----- delete start ------" + user));
        int delete = crudService.delete(query.get(0).getId());
        System.out.println(("----- delete end ------" + delete));

    }
}