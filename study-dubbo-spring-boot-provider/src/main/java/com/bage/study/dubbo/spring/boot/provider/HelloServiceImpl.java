package com.bage.study.dubbo.spring.boot.provider;

import com.bage.study.dubbo.spring.boot.api.HelloParam;
import com.bage.study.dubbo.spring.boot.api.HelloResult;
import com.bage.study.dubbo.spring.boot.api.HelloService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@DubboService
public class HelloServiceImpl implements HelloService {
    private Random random = new Random();

    @Autowired
    private UserMapper userMapper;

    @Override
    public HelloResult sayHello(HelloParam param) {
        System.out.println(new Date() + "Get param ======> " + param);

        List<UserEntity> users = userMapper.selectList(null);

        HelloResult result = new HelloResult();
        result.setCode(random.nextInt(1000));
        result.setMessage("OKKK");
        result.setUserList(users.stream().map(item -> mappingUser(item)).collect(Collectors.toList()));
        System.out.println(new Date() + "return result ======> " + result);
        return result;
    }

    private com.bage.study.dubbo.spring.boot.api.User mappingUser(UserEntity param) {
        com.bage.study.dubbo.spring.boot.api.User user = new com.bage.study.dubbo.spring.boot.api.User();
        user.setId(param.getId());
        user.setName(param.getName());
        user.setAge(Long.valueOf(param.getAge()));
        user.setEmail(param.getEmail());
        return user;
    }
}
