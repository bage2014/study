package com.bage.study.springboot.aop.annotation.crypt.impl;

import com.bage.study.springboot.aop.annotation.BaseAspect;
import com.bage.study.springboot.aop.annotation.crypt.WithMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WithAspect extends BaseAspect {

    private static final String ACTION_BEFORE = "before";
    private static final String ACTION_AFTERRETURNING = "afterReturning";
    private static final String ACTION_AFTERTHROWING = "afterThrowing";

    private static final Logger log = LoggerFactory.getLogger(WithAspect.class);

    protected void doAfterThrowing(Object target, Method method, Object[] args, WithMethod annotation, Object proxy, Object retVal, Throwable throwable) {

    }

    protected void doAfterReturning(Object target, Method method, Object[] args, WithMethod annotation, Object proxy, Object result) {

    }

    protected void doBefore(Object target, Method method, Object[] args, WithMethod annotation, Object proxy) {

    }

    @Around(value = "@annotation(com.bage.study.springboot.aop.annotation.crypt.WithMethod)")
    public Object withMethod(ProceedingJoinPoint pjp) {

        // start stopwatch
        before(pjp, null);
        Object retVal = null;
        try {
            retVal = pjp.proceed();
            afterReturning(pjp, retVal);
        } catch (Throwable throwable) {
            afterThrowing(pjp, throwable, retVal);
        }
        // stop stopwatch
        return retVal;
    }

    private void doWith(ProceedingJoinPoint pjp, Throwable throwable, String action, Object retVal) {
        // 获取参数
        Object[] args = getArgs(pjp);
        // 目标类
        Object target = getTarget(pjp);
        // 当前方法
        Method method = getMethod(pjp);
        // 代理对象
        Object proxy = getProxy(pjp);

        if (method != null) {
            WithMethod annotation = method.getAnnotation(WithMethod.class);
            if (annotation != null) {
                if (log.isDebugEnabled()) {
                    log.debug("annotation: {}", annotation);
                }
                switch (action) {
                    // 给具体实现
                    case WithAspect.ACTION_BEFORE:
                        doBefore(target, method, args, annotation, proxy);
                        break;
                    case WithAspect.ACTION_AFTERRETURNING:
                        doAfterReturning(target, method, args, annotation, proxy, retVal);
                        break;
                    case WithAspect.ACTION_AFTERTHROWING:
                        doAfterThrowing(target, method, args, annotation, proxy, retVal, throwable);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void afterThrowing(ProceedingJoinPoint pjp, Throwable throwable, Object retVal) {
        doWith(pjp, throwable, WithAspect.ACTION_AFTERTHROWING, retVal);
    }

    private void afterReturning(ProceedingJoinPoint pjp, Object retVal) {
        doWith(pjp, null, WithAspect.ACTION_AFTERRETURNING, retVal);
    }

    private void before(ProceedingJoinPoint pjp, Object retVal) {
        doWith(pjp, null, WithAspect.ACTION_BEFORE, retVal);
    }

}


