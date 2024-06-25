package com.bage.study.springboot.ann;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PageRequestAnno {

    String currentName() default "current";

    String sizeName() default "size";

}
