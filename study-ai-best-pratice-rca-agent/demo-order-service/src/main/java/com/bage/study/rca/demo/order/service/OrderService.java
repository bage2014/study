package com.bage.study.rca.demo.order.service;

import com.bage.study.rca.demo.order.client.SupplierClient;
import com.bage.study.rca.demo.order.model.CreateOrderRequest;
import com.bage.study.rca.demo.order.model.Order;
import com.bage.study.rca.demo.order.model.SupplyCheckResult;
import com.bage.study.rca.demo.order.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * 订单服务：下单前远程校验供应库存，订单持久化到 H2。
 */
@Service
public class OrderService {

    private final SupplierClient supplierClient;
    private final OrderRepository orderRepository;

    public OrderService(SupplierClient supplierClient, OrderRepository orderRepository) {
        this.supplierClient = supplierClient;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(CreateOrderRequest request) {
        SupplyCheckResult supply;
        try {
            supply = supplierClient.check(request.skuId(), request.quantity());
        } catch (ResourceAccessException e) {
            // 连接超时 / 读取超时都会落到这里：SocketTimeoutException
            throw new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT,
                    "调用供应服务超时或连接失败: " + e.getMessage(), e);
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "供应服务返回异常: " + e.getMessage(), e);
        }

        if (!supply.available()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "库存不足: skuId=%s, requested=%d, available=%d"
                            .formatted(request.skuId(), request.quantity(), supply.availableQuantity()));
        }

        BigDecimal totalAmount = supply.unitPrice().multiply(BigDecimal.valueOf(request.quantity()));
        Order order = new Order(request.skuId(), request.quantity(),
                supply.unitPrice(), totalAmount, "CREATED", Instant.now());
        return orderRepository.save(order);
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "订单不存在: id=" + id));
    }

    public List<Order> listOrders() {
        return orderRepository.findAll();
    }
}
