package com.bage.study.springboot.aop.annotation.gray;

import java.lang.reflect.Method;

public interface OrderGrayFlowService {
    boolean isEffect(OrderGrayFlow annotation, Method method, Object[] args);
}
