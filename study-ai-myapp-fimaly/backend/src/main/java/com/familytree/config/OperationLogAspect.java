package com.familytree.config;

import com.familytree.model.OperationLog;
import com.familytree.service.OperationLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
public class OperationLogAspect {
    @Autowired
    private OperationLogService operationLogService;

    @Pointcut("execution(* com.familytree.controller.*.*(..))")
    public void controllerPointcut() {}

    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void logOperation(JoinPoint joinPoint, Object result) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Long userId = getUserIdFromAuthentication(authentication);

            // 检查userId是否为null，避免数据完整性异常
            if (userId != null) {
                OperationLog log = new OperationLog();
                log.setOperatorId(userId);
                log.setOperatorName(username);
                log.setOperationType(getOperationType(joinPoint));
                log.setOperationDescription(getOperationDescription(joinPoint));
                log.setOperationTime(LocalDateTime.now());

                operationLogService.createLog(log);
            }
        }
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        // 这里需要根据实际的用户信息获取方式来实现
        // 从UserDetails中获取用户名（即userId）
        Object principal = authentication.getPrincipal();
        if (principal instanceof org.springframework.security.core.userdetails.User) {
            org.springframework.security.core.userdetails.User userDetails = (org.springframework.security.core.userdetails.User) principal;
            try {
                return Long.parseLong(userDetails.getUsername());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String getOperationType(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        if (methodName.startsWith("create")) {
            return "CREATE";
        } else if (methodName.startsWith("update")) {
            return "UPDATE";
        } else if (methodName.startsWith("delete")) {
            return "DELETE";
        } else if (methodName.startsWith("get") || methodName.startsWith("find")) {
            return "READ";
        }
        return "OTHER";
    }

    private String getOperationDescription(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        return className + "." + methodName + "()";
    }
}
