package com.study.common.metrics.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Metric {

    String value() default "";

    boolean recordDuration() default true;

    boolean recordCount() default true;

    boolean recordError() default true;
}