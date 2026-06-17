package com.study.common.metrics.aspect;

import com.study.common.metrics.annotation.Metric;
import com.study.common.metrics.service.MetricService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class MetricAspect {

    private final MetricService metricService;

    @Around("@annotation(metric)")
    public Object metricAround(ProceedingJoinPoint joinPoint, Metric metric) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String metricName = metric.value().isEmpty() ? className + "." + methodName : metric.value();

        long startTime = System.currentTimeMillis();
        boolean success = true;

        if (metric.recordCount()) {
            metricService.incrementCount(metricName);
        }

        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            success = false;
            if (metric.recordError()) {
                metricService.incrementErrorCount(metricName);
            }
            throw e;
        } finally {
            if (metric.recordDuration()) {
                long duration = System.currentTimeMillis() - startTime;
                metricService.recordDuration(metricName, duration, success);
            }
        }
    }

    @Around("execution(* com.study..*Service.*(..))")
    public Object serviceMetricAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String metricName = "service." + className + "." + methodName;

        long startTime = System.currentTimeMillis();
        boolean success = true;

        metricService.incrementCount(metricName);

        try {
            Object result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            success = false;
            metricService.incrementErrorCount(metricName);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            metricService.recordDuration(metricName, duration, success);
        }
    }
}