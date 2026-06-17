package com.study.common.exception.aspect;

import com.study.common.exception.BusinessException;
import com.study.common.exception.ErrorCode;
import com.study.common.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExceptionAspect {

    @Around("execution(* com.study..*Controller.*(..))")
    public Object exceptionHandler(ProceedingJoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        try {
            return joinPoint.proceed();
        } catch (BusinessException e) {
            log.warn("Business exception in {}#{}, code: {}, message: {}", className, methodName, e.getCode(), e.getMessage());
            return ResponseEntity.ok(ErrorResponse.of(e.getCode(), e.getMessage(), e.getData()));
        } catch (IllegalArgumentException e) {
            log.warn("Illegal argument exception in {}#{}, message: {}", className, methodName, e.getMessage());
            return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.PARAM_ERROR.getCode(), e.getMessage()));
        } catch (Exception e) {
            log.error("Unexpected exception in {}#{}, message: {}", className, methodName, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ErrorResponse.of(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMessage()));
        }
    }
}