package com.bage.study.grayvalidator.core.resolver;

import com.bage.study.grayvalidator.core.FieldResolver;
import com.bage.study.grayvalidator.core.config.OrderUserConfig;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class OrderToUserFieldResolver implements FieldResolver {

    private static final String FIELD_ORDER_ID = "orderId";

    private final OrderUserConfig config;

    public OrderToUserFieldResolver(OrderUserConfig config) {
        this.config = config;
    }

    @Override
    public Optional<String> resolve(String fieldName, String rawValue, HttpServletRequest request) {
        if (!FIELD_ORDER_ID.equals(fieldName)) return Optional.empty();
        return Optional.ofNullable(config.getOrderUserMap().get(rawValue));
    }
}