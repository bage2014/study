package com.bage.study.springboot.aop.order;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
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
@Order(3)
@Component
public class HelloAopOrderServiceExecutor3 {

    @Before("execution(public * com.bage.study.springboot.aop.order.HelloAopOrderService.hello2(..))")
    public void before() throws Throwable {
        System.out.println("HelloAopOrderServiceExecutor3 Before ing");
    }

}