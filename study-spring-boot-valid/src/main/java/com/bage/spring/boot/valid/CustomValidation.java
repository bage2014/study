package com.bage.spring.boot.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidation {

    String message() default "Invalid field value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}


