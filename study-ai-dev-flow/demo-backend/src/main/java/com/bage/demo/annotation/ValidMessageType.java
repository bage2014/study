package com.bage.demo.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MessageTypeValidator.class)
public @interface ValidMessageType {
    String message() default "Invalid message type. Must be one of: TEXT, IMAGE, VIDEO, FILE, SYSTEM";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
