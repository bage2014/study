package com.bage.study.springboot.aop.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BaseAspect {

    private static final Logger log = LoggerFactory.getLogger(BaseAspect.class);

    public Object getProxy(ProceedingJoinPoint pjp) {
        Object proxy = pjp.getThis();
        if (log.isDebugEnabled()) {
            log.debug("proxy：{}", proxy);
        }
        return proxy;
    }

    public Method getMethod(ProceedingJoinPoint pjp) {
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
    public Object getTarget(ProceedingJoinPoint pjp) {
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
    public Object[] getArgs(ProceedingJoinPoint pjp) {
        Object[] args = pjp.getArgs();
        if (log.isDebugEnabled()) {
            log.debug("args：{}", Arrays.toString(args));
        }
        return args;
    }

}
