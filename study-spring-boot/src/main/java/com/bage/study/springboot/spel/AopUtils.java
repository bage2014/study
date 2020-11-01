package com.bage.study.springboot.spel;

import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class AopUtils {

    public static Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Class[] parameterTypes = Arrays.stream(getArgs(joinPoint)).map(item -> item.getClass()).collect(Collectors.toList()).toArray(new Class[]{});
        return joinPoint.getTarget().getClass().getMethod(joinPoint.getSignature().getName(), parameterTypes);
    }

    public static Object[] getArgs(ProceedingJoinPoint joinPoint) {
        return joinPoint.getArgs();
    }

}
