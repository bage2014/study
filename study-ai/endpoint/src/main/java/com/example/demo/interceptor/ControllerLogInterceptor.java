package com.example.demo.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class ControllerLogInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(ControllerLogInterceptor.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String timestamp = LocalDateTime.now().format(formatter);
        String params = "";
        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            try {
                // 简化处理，实际项目中可使用流读取请求体
                params = "POST/PUT body: 请使用 @RequestBody 解析"; 
            } catch (Exception e) {
                logger.error("读取请求体失败", e);
            }
        } else {
            Map<String, String[]> parameterMap = request.getParameterMap();
            params = parameterMap.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                    .collect(Collectors.joining(", "));
        }
        logger.info("[{}] 请求路径: {}, 请求方法: {}, 请求参数: {}", timestamp, request.getRequestURI(), request.getMethod(), params);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 这里可以记录响应信息，由于响应体可能较大，简单记录状态码
        String timestamp = LocalDateTime.now().format(formatter);
        logger.info("[{}] 请求路径: {}, 响应状态码: {}", timestamp, request.getRequestURI(), response.getStatus());
    }
}