package com.bage.study.best.practice;

import com.bage.study.best.practice.mock.UserServiceMockImpl;
import com.bage.study.best.practice.repo.UserMapper;
import com.bage.study.best.practice.repo.UserEntity;
import com.bage.study.best.practice.model.User;
import com.bage.study.best.practice.service.UserService;
import com.bage.study.best.practice.utils.DateUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;
import java.util.List;

@SpringBootApplication
@MapperScan("com.bage.study.best.practice.repo")
public class BestPracticeApplication implements CommandLineRunner {

    @Resource
    private UserServiceMockImpl userServiceMock;
    @Resource
    private UserMapper userMapper;

    public static void main(String args[]) {
        SpringApplication.run(BestPracticeApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        System.out.println(("----- selectAll method test ------"));
        List<UserEntity> userList = userMapper.selectList(null);
        System.out.println(("----- selectAll size ------" + userList.size()));
        for (UserEntity user : userList) {
            System.out.println(user);
        }

        //        int max = 1000;
//        List<User> initData = userServiceMock.getInitData(max);
//        for (int i = 0; i < initData.size(); i++) {
//            if(i % 1000 == 0){
//                Thread.sleep(500L);
//            }
//            User userPro = initData.get(i);
//            int insert = userMapper.insert(mapping(userPro));
//            System.out.println(insert);
//        }
    }

    private UserEntity mapping(User userPro) {
        UserEntity entity = new UserEntity();
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