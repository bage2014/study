package com.bage.demo.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class MessageTypeValidator implements ConstraintValidator<ValidMessageType, String> {

    private static final Set<String> VALID_TYPES = new HashSet<>(
            Arrays.asList("TEXT", "IMAGE", "VIDEO", "FILE", "SYSTEM")
    );

    @Override
    public void initialize(ValidMessageType constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return VALID_TYPES.contains(value.toUpperCase());
    }
}
