package com.bage.study.dubbo.spring.boot.provider;

import com.bage.study.dubbo.spring.boot.api.HelloParam;
import com.bage.study.dubbo.spring.boot.api.HelloResult;
import com.bage.study.dubbo.spring.boot.api.HelloService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@DubboService(timeout = 900)
public class HelloServiceImpl implements HelloService {
    private Random random = new Random();

    @Autowired
    private UserMapper userMapper;

    @Override
    public HelloResult sayHello(HelloParam param) {
////客户端隐示设置值
//        RpcContext.getContext().setAttachment("index", "1"); // 隐式传参，后面的远程调用都会隐
        //服务端隐示获取值
        String index = RpcContext.getContext().getAttachment("index");

        System.out.println(new Date() + "getAttachment param ======> " + index);

        System.out.println(new Date() + "Get param ======> " + param);
        try {
            Thread.sleep(new Random().nextInt(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
