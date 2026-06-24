package com.bage.study.grayvalidator.core;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface FieldResolver {
    Optional<String> resolve(String fieldName, String rawValue, HttpServletRequest request);
}