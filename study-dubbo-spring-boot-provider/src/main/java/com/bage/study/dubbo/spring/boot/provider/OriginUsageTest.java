package com.bage.study.dubbo.spring.boot.provider;

import com.bage.study.dubbo.spring.boot.api.HelloParam;
import com.bage.study.dubbo.spring.boot.api.HelloResult;
import com.bage.study.dubbo.spring.boot.api.HelloService;
import org.apache.dubbo.config.*;

public class OriginUsageTest {

    private static final int port = 12345;
    private static final String remoteUrl = "dubbo://127.0.0.1:" + port + "/" + HelloService.class.getName();

    public static void main(String[] args) {
        OriginUsageTest originUsage = new OriginUsageTest();
        originUsage.openServer();
        HelloParam helloParam = new HelloParam();
        helloParam.setId(123L);
        helloParam.setName("123L");
        HelloResult hhh = originUsage.buildRemoteService().sayHello(helloParam);
        System.out.println(hhh);
    }

    public void openServer() {
        ApplicationConfig config = new ApplicationConfig();
        config.setName("simple-app");
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(port);
        protocolConfig.setThreads(20);
        ServiceConfig<HelloService> serviceConfig = new ServiceConfig();
        serviceConfig.setApplication(config);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setRegistry(new RegistryConfig(RegistryConfig.NO_AVAILABLE));
        serviceConfig.setInterface(HelloService.class);
        serviceConfig.setRef(new HelloServiceImpl());
        serviceConfig.export();
    }

    // 构建远程服务对象
    public HelloService buildRemoteService() {
        System.out.println(remoteUrl);
        ApplicationConfig application = new ApplicationConfig();
        application.setName("young-app");
        ReferenceConfig<HelloService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setApplication(application);
        referenceConfig.setInterface(HelloService.class);
        referenceConfig.setUrl(remoteUrl);
        HelloService userService = referenceConfig.get();
        return userService;
    }
}
