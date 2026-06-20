package com.study.common.log.aspect;

import com.study.common.log.annotation.Log;
import com.study.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("@annotation(logAnnotation)")
    public Object logAround(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String logValue = logAnnotation.value().isEmpty() ? methodName : logAnnotation.value();

        if (logAnnotation.logParams()) {
            Object[] args = joinPoint.getArgs();
            String params = Arrays.toString(args);
            log.info("[{}] {}#{}, params: {}", DateUtil.dateTime2String(DateUtil.now()), className, methodName, params);
        }

        Object result = null;
        try {
            result = joinPoint.proceed();
            if (logAnnotation.logResult()) {
                log.info("[{}] {}#{}, result: {}", DateUtil.dateTime2String(DateUtil.now()), className, methodName, result);
            }
            return result;
        } catch (Throwable e) {
            if (logAnnotation.logException()) {
                log.error("[{}] {}#{}, exception: {}", DateUtil.dateTime2String(DateUtil.now()), className, methodName, e.getMessage(), e);
            }
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("[{}] {}#{}, duration: {}ms", DateUtil.dateTime2String(DateUtil.now()), className, methodName, duration);
        }
    }

    @Around("execution(* com.study..*Controller.*(..))")
    public Object controllerLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] args = joinPoint.getArgs();
        String params = Arrays.toString(args);

        log.info("[{}] Controller enter: {}#{}, params: {}", DateUtil.dateTime2String(DateUtil.now()), className, methodName, params);

        Object result = null;
        try {
            result = joinPoint.proceed();
            log.info("[{}] Controller exit: {}#{}, result: {}", DateUtil.dateTime2String(DateUtil.now()), className, methodName, result);
            return result;
        } catch (Throwable e) {
            log.error("[{}] Controller error: {}#{}, exception: {}", DateUtil.dateTime2String(DateUtil.now()), className, methodName, e.getMessage(), e);
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("[{}] Controller {}#{}, duration: {}ms", DateUtil.dateTime2String(DateUtil.now()), className, methodName, duration);
        }
    }
}
