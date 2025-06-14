package com.bage.study.springboot.aop.annotation.gray;

import com.bage.study.springboot.spel.AopUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author rh_lu
 */

@Aspect
@Slf4j
@Component
public class OrderGrayFlowAspect {
    @Autowired
    private OrderGrayFlowLogic orderGrayFlowLogic;

    @Around(value = "@annotation(com.ctrip.train.trnorder.core.biz.temp.gray.OrderGrayFlow)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = null;
        Object result = null;
        Method method = null;
        OrderGrayFlow annotation = null;
        try {
            method = AopUtils.getMethod(pjp);
            args = AopUtils.getArgs(pjp);
            annotation = AopUtils.getMethodAnnotation(method, OrderGrayFlow.class);
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        try {
            // 命中灰度
            if (orderGrayFlowLogic.isGray(annotation, method, args)) {
                Object bean = orderGrayFlowLogic.getBean(annotation, method);
                if (bean != null) {
                    return orderGrayFlowLogic.invokeSync(bean, annotation, method, args);
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return pjp.proceed(); // 兜底都 老逻辑
    }
}
