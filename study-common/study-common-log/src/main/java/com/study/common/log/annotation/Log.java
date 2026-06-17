package com.study.common.log.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

    String value() default "";

    boolean logParams() default true;

    boolean logResult() default true;

    boolean logException() default true;
}