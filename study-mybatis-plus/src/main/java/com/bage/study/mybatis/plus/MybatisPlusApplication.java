package com.bage.study.mybatis.plus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@MapperScan("com.bage.study.mybatis.plus")
public class MybatisPlusApplication implements CommandLineRunner {

    @Resource
    private InitService initService;
    @Resource
    private CrudService crudService;
    private Random random = new Random();


    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }

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

        User user2 = new User();
        user2.setId(System.currentTimeMillis() + 1);
        user2.setAge(nextInt);
        user2.setEmail(random.nextInt(100000) + "@qq.com");
        user2.setName("bage" + nextInt);
        System.out.println(("----- insert batch start ------" + user2));
        int insertBatch = crudService.insertBatch(Collections.singletonList(user2));
        System.out.println(("----- insert batch end ------" + insertBatch));

        String name = "bage" + nextInt;
        System.out.println(("----- query start ------" + name));
        List<User> query = crudService.query(name);
        System.out.println(("----- query end ------" + query));

        String keyword = "bage" + nextInt;
        System.out.println(("----- query page start ------" + keyword));
        Page page = crudService.page(1, 10, keyword);
        System.out.println(("----- query page getCurrent ------" + page.getCurrent()));
        System.out.println(("----- query page getTotal ------" + page.getTotal()));
        System.out.println(("----- query page getRecords ------" + page.getRecords()));

        user.setEmail(user.getEmail() + "-update@.cpm");
        System.out.println(("----- update start ------" + user));
        int update = crudService.update(user);
        System.out.println(("----- update end ------" + update));

        System.out.println(("----- delete start ------" + user));
        int delete = crudService.delete(query.get(0).getId());
        System.out.println(("----- delete end ------" + delete));

    }
}