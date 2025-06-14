package com.bage.study.springboot.aop.annotation.gray;

import com.bage.study.springboot.spel.SpELUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.EvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author rh_lu
 */
@Slf4j
@Component
public class OrderGrayFlowCommonLogic {
    public String getKeyBySpEL(OrderGrayFlow annotation, Method method, Object[] args) {
        EvaluationContext evaluationContext = SpELUtils.buildEvaluationContext(method, args);
        String key = SpELUtils.parseSpELValue(evaluationContext, annotation.keySpEL(), String.class, "");
        String[] split = {"", ""};
        return key;
    }
}
