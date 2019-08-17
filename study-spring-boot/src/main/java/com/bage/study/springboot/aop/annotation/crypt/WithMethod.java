package com.bage.study.springboot.aop.annotation.crypt;


import java.lang.annotation.*;


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface WithMethod {

    /**
     * 参数类
     *
     * @return
     */
    Class<?>[] clsOfParam() default {Object.class};

    /**
     * 前置触发
     *
     * @return
     */
    boolean before() default false;

    /**
     * 正常执行后触发
     *
     * @return
     */
    boolean afterReturning() default false;

    /**
     * 异常后触发
     *
     * @return
     */
    boolean afterThrowing() default false;


}
