package com.bage.study.rca.demo.order.repository;

import com.bage.study.rca.demo.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
