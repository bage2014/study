package com.bage.spring.boot.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * groups这里主要进行将validator进行分类，不同的类group中会执行不同的validator操作
 * payload 主要是针对bean的，使用不多。
 */
@Documented
@Constraint(validatedBy = CustomValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidation {

    String message() default "Invalid field value";

    Class<?>[] groups() default {};

    /**
     * Payload
     * @return
     */
    Class<? extends Payload>[] payload() default {};

}


