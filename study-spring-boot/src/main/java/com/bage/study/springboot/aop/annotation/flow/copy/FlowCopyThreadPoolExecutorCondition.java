package com.bage.study.springboot.aop.annotation.flow.copy;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Stream;

public class FlowCopyThreadPoolExecutorCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String[] beanNames = context.getBeanFactory().getBeanNamesForType(ThreadPoolExecutor.class);
        return !Stream.of(beanNames).anyMatch(beanName -> beanName.equals("flowCopyThreadPoolExecutor"));
    }
}
