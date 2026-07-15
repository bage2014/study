package com.example.service;

import com.example.dto.OrderCreateRequest;
import com.example.dto.OrderListResponse;
import com.example.dto.OrderResponse;
import com.example.dto.OrderStatusUpdateRequest;
import com.example.dto.PageRequest;
import com.example.entity.Order;
import com.example.entity.OrderStatus;
import com.example.exception.InvalidOrderStatusTransitionException;
import com.example.exception.OrderNotFoundException;
import com.example.repository.OrderRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        Order order = Order.builder()
                .productName(request.getProductName())
                .quantity(request.getQuantity())
                .amount(request.getAmount())
                .status(OrderStatus.PENDING_PAYMENT)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .deleted(false)
                .build();
        order = orderRepository.save(order);
        return OrderResponse.fromEntity(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderListResponse> listOrders(PageRequest pageRequest) {
        Specification<Order> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isFalse(root.get("deleted")));
            if (pageRequest.getStatus() != null && !pageRequest.getStatus().isEmpty()) {
                predicates.add(cb.equal(root.get("status"), OrderStatus.valueOf(pageRequest.getStatus())));
            }
            if (pageRequest.getStartDate() != null && !pageRequest.getStartDate().isEmpty()) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), LocalDateTime.parse(pageRequest.getStartDate())));
            }
            if (pageRequest.getEndDate() != null && !pageRequest.getEndDate().isEmpty()) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), LocalDateTime.parse(pageRequest.getEndDate())));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Order> orders = orderRepository.findAll(spec,
                PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), Sort.by(Sort.Direction.DESC, "createdAt")));
        return orders.map(OrderListResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        return OrderResponse.fromEntity(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatusUpdateRequest request) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        OrderStatus newStatus = request.getStatus();
        if (!isValidTransition(order.getStatus(), newStatus)) {
            throw new InvalidOrderStatusTransitionException(
                    "Cannot transition from " + order.getStatus() + " to " + newStatus);
        }
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepository.save(order);
        return OrderResponse.fromEntity(order);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + id));
        order.setDeleted(true);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case PENDING_PAYMENT -> next == OrderStatus.PAID || next == OrderStatus.CANCELLED;
            case PAID -> next == OrderStatus.SHIPPED || next == OrderStatus.CANCELLED;
            case SHIPPED -> next == OrderStatus.COMPLETED;
            case COMPLETED, CANCELLED -> false;
        };
    }
}