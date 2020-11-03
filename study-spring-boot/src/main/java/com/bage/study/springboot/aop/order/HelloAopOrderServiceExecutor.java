package com.bage.study.springboot.aop.order;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 *
 * AfterReturning 抛异常后不执行！！
 * After 不管是否抛异常都执行
 *
 * 执行结果1：
 * <p>
 * Around start
 * Before ing
 * Around end
 * After ing
 * AfterReturning ing
 * ---------------->>>>>>>>>>>>>>------------
 * - Around - Before - Around - After - AfterReturning
 *
 * <p>
 * 执行结果2：
 * Around start
 * Before ing
 * After ing
 * AfterThrowing ing
 * ---------------->>>>>>>>>>>>>>------------
 * - Around - Before - After - AfterThrowing
 *
 */
@Aspect
@Component
public class HelloAopOrderServiceExecutor {

    @Around("execution(public * com.bage.study.springboot.aop.order.HelloAopOrderService.hello(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("Around start");
        Object proceed = pjp.proceed();
        System.out.println("Around end");
        return proceed;
    }

    @After("execution(public * com.bage.study.springboot.aop.order.HelloAopOrderService.hello(..))")
    public void after() throws Throwable {
        System.out.println("After ing");
    }

    @Before("execution(public * com.bage.study.springboot.aop.order.HelloAopOrderService.hello(..))")
    public void before() throws Throwable {
        System.out.println("Before ing");
    }

    @AfterReturning("execution(public * com.bage.study.springboot.aop.order.HelloAopOrderService.hello(..))")
    public void afterReturning() throws Throwable {
        System.out.println("AfterReturning ing");
    }

    @AfterThrowing("execution(public * com.bage.study.springboot.aop.order.HelloAopOrderService.hello(..))")
    public void afterThrowing() throws Throwable {
        System.out.println("AfterThrowing ing");
    }

}