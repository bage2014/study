
package com.bage.study.springboot.aop.annotation.gray;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface OrderGrayFlow {
    Class toClass(); // 到类

    String keySpEL() default "";

    String copyToMethod() default ""; // 到类对应方法，默认是相同方法

}
