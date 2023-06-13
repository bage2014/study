package com.bage.study.common.repo.mysql.mybatis.plus.beta;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@MapperScan("com.bage.study.common.repo.mysql.mybatis.plus")
public class MysqlMybatisPlusApplication implements CommandLineRunner {

    @Resource
    private UserMapper userMapper;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    public static void main(String args[]) {
        SpringApplication.run(MysqlMybatisPlusApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        System.out.println(("----- selectAll size ------" + userList.size()));
        for (User user : userList) {
            System.out.println(user);
        }

    }
}