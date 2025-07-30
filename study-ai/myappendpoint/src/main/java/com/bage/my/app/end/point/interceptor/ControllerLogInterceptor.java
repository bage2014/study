package com.bage.my.app.end.point.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.bage.my.app.end.point.util.JsonUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ControllerLogInterceptor implements HandlerInterceptor {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 使用 ContentCachingRequestWrapper 包装原始请求
        String timestamp = LocalDateTime.now().format(formatter);
        String params = "";

        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            try {
                params = "Post unsupported log info ";
                // 读取请求体
            } catch (Exception e) {
                log.error("读取请求体失败", e);
                // 发生异常时，尝试使用参数映射
                params = JsonUtil.toJson(request.getParameterMap());
            }
        } else {
            Map<String, String[]> parameterMap = request.getParameterMap();
            params = parameterMap.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                    .collect(Collectors.joining(", "));
        }

        log.info("[{}] 请求路径: {}, 请求方法: {}, 请求参数: {}", timestamp, request.getRequestURI(), request.getMethod(), params);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 这里可以记录响应信息，由于响应体可能较大，简单记录状态码
        String timestamp = LocalDateTime.now().format(formatter);
        log.info("[{}] 请求路径: {}, 响应状态码: {}", timestamp, request.getRequestURI(), response.getStatus());
    }
}