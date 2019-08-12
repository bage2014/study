package com.bage.study.springboot.aop.annotation.impl;

import com.bage.study.springboot.aop.annotation.WithMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class WithAspect {

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

    @Around(value = "@annotation(com.bage.study.springboot.aop.annotation.WithMethod)")
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

    private Object getProxy(ProceedingJoinPoint pjp) {
        Object proxy = pjp.getThis();
        if (log.isDebugEnabled()) {
            log.debug("proxy：{}", proxy);
        }
        return proxy;
    }

    private Method getMethod(ProceedingJoinPoint pjp) {
        // 当前方法
        Method method = null;
        try {
            Signature signature = pjp.getSignature();

            // 获取参数
            Object[] args = getArgs(pjp);

            // 目标类
            Object target = getTarget(pjp);

            Class[] parameterTypes = Arrays.stream(args).map(item -> item.getClass()).collect(Collectors.toList()).toArray(new Class[]{});
            method = target.getClass().getMethod(signature.getName(), parameterTypes);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage(), e);
        }
        if (log.isDebugEnabled()) {
            log.debug("method：{}", method);
        }
        return method;
    }

    /**
     * 目标类
     *
     * @param pjp
     * @return
     */
    private Object getTarget(ProceedingJoinPoint pjp) {
        Object target = pjp.getTarget();
        if (log.isDebugEnabled()) {
            log.debug("target：{}", target);
        }
        return target;
    }

    /**
     * 获取参数
     *
     * @param pjp
     * @return
     */
    private Object[] getArgs(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (log.isDebugEnabled()) {
            log.debug("args：{}", Arrays.toString(args));
        }
        return args;
    }

}


