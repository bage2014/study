package com.familytree.exception;

import com.familytree.dto.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_TEMPLATE = "[{}] {} - {}";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleBusinessException(BusinessException ex) {
        log.warn(LOG_TEMPLATE, ex.getErrorCode().getCode(), ex.getErrorCode().getMessage(), ex.getDetailMessage());

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("errorCode", ex.getErrorCode().getCode());
        errorDetails.put("errorMessage", ex.getErrorCode().getMessage());
        if (ex.getDetailMessage() != null) {
            errorDetails.put("detailMessage", ex.getDetailMessage());
        }

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ApiResponse.error(ex.getErrorCode().getCode(), ex.getErrorCode().getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "字段验证失败",
                        (existing, replacement) -> existing
                ));

        log.warn("[VALIDATION_ERROR] 参数校验失败: {}", errors);

        ApiResponse<Map<String, String>> response = ApiResponse.error("G002", "参数校验失败");
        response.setData(errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        log.warn("[VALIDATION_ERROR] 约束校验失败: {}", message);

        return ResponseEntity.badRequest()
                .body(ApiResponse.error("G002", message));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<String>> handleMissingParameterException(MissingServletRequestParameterException ex) {
        log.warn("[VALIDATION_ERROR] 缺少请求参数: {}", ex.getParameterName());

        return ResponseEntity.badRequest()
                .body(ApiResponse.error("G002", "缺少请求参数: " + ex.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = String.format("参数 '%s' 类型错误，期望值类型: %s",
                ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "未知");

        log.warn("[VALIDATION_ERROR] 参数类型错误: {}", message);

        return ResponseEntity.badRequest()
                .body(ApiResponse.error("G002", message));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<String>> handleBadCredentialsException(BadCredentialsException ex) {
        log.warn("[AUTH_ERROR] 认证失败: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("A002", "用户名或密码错误"));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException ex) {
        log.warn("[AUTH_ERROR] 认证异常: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error("A001", "认证失败"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<String>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("[AUTH_ERROR] 访问被拒绝: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error("P001", "权限不足"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("[VALIDATION_ERROR] 非法参数: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(ApiResponse.error("G002", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException ex) {
        log.warn("[STATE_ERROR] 非法状态: {}", ex.getMessage());

        return ResponseEntity.badRequest()
                .body(ApiResponse.error("G004", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        log.warn("[RUNTIME_ERROR] 运行时异常: {}", ex.getMessage(), ex);

        if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("G001", ex.getMessage()));
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("G003", ex.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse<String>> handleNullPointerException(NullPointerException ex) {
        log.error("[INTERNAL_ERROR] 空指针异常: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.serverError("服务器内部错误"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGenericException(Exception ex) {
        log.error("[INTERNAL_ERROR] 未处理的异常: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.serverError("服务器内部错误"));
    }
}