package com.bage.spring.boot.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class CustomValidator implements ConstraintValidator<CustomValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 在这里编写自定义校验逻辑
        System.out.println("CustomValidator:" + value);
        System.out.println("context:" + context);
        return value != null && value.startsWith("abc");
    }
}

