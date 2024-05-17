package com.bage.study.common.repo.mysql.mybatis.plus.pro;

import com.bage.study.common.repo.mysql.mybatis.plus.pro.dao.UserMapper;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.dao.UserProEntity;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.model.UserPro;
import com.bage.study.common.repo.mysql.mybatis.plus.pro.service.UserProDataService;
import com.bage.study.common.repo.mysql.mybatis.plus.utils.DateUtils;
import com.baomidou.mybatisplus.annotation.TableField;
import org.joda.time.LocalDateTime;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;

@SpringBootApplication
@MapperScan("com.bage.study.common.repo.mysql.mybatis.plus.pro.dao")
public class DataInitApplication implements CommandLineRunner {

    @Resource
    private UserProDataService userProDataService;
    @Resource
    private UserMapper userMapper;

    public static void main(String args[]) {
        SpringApplication.run(DataInitApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        int max = 1000;
        List<UserPro> initData = userProDataService.getInitData(max);
        for (int i = 0; i < initData.size(); i++) {
            if(i % 1000 == 0){
                Thread.sleep(500L);
            }
            UserPro userPro = initData.get(i);
            int insert = userMapper.insert(mapping(userPro));
            System.out.println(insert);
        }
    }

    private UserProEntity mapping(UserPro userPro) {
        UserProEntity entity = new UserProEntity();
        entity.setId(userPro.getId());
        entity.setFirstName(userPro.getFirstName());
        entity.setLastName(userPro.getLastName());
        entity.setSex(userPro.getSex().getCode());
        entity.setBirthday(DateUtils.localDate2Date(userPro.getBirthday()));
        entity.setAddress(userPro.getAddress());
        entity.setEmail(userPro.getEmail());
        entity.setPhone(userPro.getPhone());
        entity.setRemark(userPro.getRemark());
        return entity;
    }
}