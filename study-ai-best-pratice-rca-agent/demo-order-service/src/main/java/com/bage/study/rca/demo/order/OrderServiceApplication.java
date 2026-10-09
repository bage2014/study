package com.bage.study.rca.demo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RCA Demo - 订单服务启动类（端口 8083）。
 * 下单前通过 RestClient 调用供应服务校验库存，用于构造跨服务超时级联等 RCA 场景。
 */
@SpringBootApplication
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
