package com.bage.study.springboot.enable;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MyServiceConfig.class})
public @interface EnableMyService {
    //传入包名
    String[] packages() default "";
}