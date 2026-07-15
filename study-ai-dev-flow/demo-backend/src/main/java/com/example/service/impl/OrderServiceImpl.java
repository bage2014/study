package com.example.service.impl;

import com.example.entity.Order;
import com.example.entity.OrderStatus;
import com.example.dto.OrderCreateRequest;
import com.example.dto.OrderUpdateStatusRequest;
import com.example.dto.OrderPageResponse;
import com.example.dto.OrderQueryCriteria;
import com.example.exception.OrderNotFoundException;
import com.example.exception.InvalidOrderStatusTransitionException;
import com.example.repository.OrderRepository;
import com.example.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public Order createOrder(OrderCreateRequest request) {
        log.info("开始创建订单");
        Order order = new Order();
        order.setProductName(request.getProductName());
        order.setQuantity(request.getQuantity());
        order.setAmount(request.getAmount());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setDeleted(false);
        Order savedOrder = orderRepository.save(order);
        log.info("订单创建成功，订单ID: {}", savedOrder.getId());
        return savedOrder;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderPageResponse listOrders(OrderQueryCriteria criteria, Pageable pageable) {
        log.info("分页查询订单列表");
        Specification<Order> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 只查询未逻辑删除的订单
            predicates.add(cb.isFalse(root.get("deleted")));
            if (criteria.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), criteria.getStatus()));
            }
            if (criteria.getStartTime() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), criteria.getStartTime()));
            }
            if (criteria.getEndTime() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), criteria.getEndTime()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orderPage = orderRepository.findAll(spec, pageable);
        OrderPageResponse response = new OrderPageResponse();
        response.setOrders(orderPage.getContent());
        response.setTotalElements(orderPage.getTotalElements());
        response.setTotalPages(orderPage.getTotalPages());
        response.setCurrentPage(pageable.getPageNumber());
        response.setPageSize(pageable.getPageSize());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> getOrderById(Long orderId) {
        log.info("查询订单详情，订单ID: {}", orderId);
        return orderRepository.findByIdAndDeletedFalse(orderId);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderUpdateStatusRequest request) {
        log.info("更新订单状态，订单ID: {}, 目标状态: {}", orderId, request.getStatus());
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("订单不存在，ID: " + orderId));
        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();
        if (!isValidTransition(currentStatus, newStatus)) {
            throw new InvalidOrderStatusTransitionException(
                    "无效的状态转换，从 " + currentStatus + " 到 " + newStatus);
        }
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        log.info("订单状态更新成功，订单ID: {}, 新状态: {}", orderId, newStatus);
        return updatedOrder;
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        log.info("删除订单，订单ID: {}", orderId);
        Order order = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new OrderNotFoundException("订单不存在，ID: " + orderId));
        order.setDeleted(true);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
        log.info("订单逻辑删除成功，订单ID: {}", orderId);
    }

    /**
     * 验证订单状态转换是否合法
     * @param current 当前状态
     * @param target 目标状态
     * @return 是否合法
     */
    private boolean isValidTransition(OrderStatus current, OrderStatus target) {
        if (current == target) {
            return true;
        }
        switch (current) {
            case PENDING_PAYMENT:
                return target == OrderStatus.PAID || target == OrderStatus.CANCELLED;
            case PAID:
                return target == OrderStatus.SHIPPED || target == OrderStatus.CANCELLED;
            case SHIPPED:
                return target == OrderStatus.COMPLETED;
            case COMPLETED:
            case CANCELLED:
                return false;
            default:
                return false;
        }
    }
}