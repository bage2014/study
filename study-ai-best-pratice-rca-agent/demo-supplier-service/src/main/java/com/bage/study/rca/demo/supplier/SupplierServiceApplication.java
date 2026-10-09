package com.bage.study.rca.demo.supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RCA Demo - 供应服务启动类（端口 8084）。
 * 作为订单服务的下游，用于构造请求超时、错误传播等 RCA 场景。
 */
@SpringBootApplication
public class SupplierServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierServiceApplication.class, args);
    }
}
