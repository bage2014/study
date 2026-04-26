package com.familytree.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoggingInterceptor extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";
    private static final String START_TIME = "startTime";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        MDC.put(TRACE_ID, traceId);
        MDC.put("method", request.getMethod());
        MDC.put("uri", request.getRequestURI());
        MDC.put("clientIp", getClientIp(request));

        response.setHeader("X-Trace-Id", traceId);

        try {
            log.info("[HTTP_REQUEST] {} {} from {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    getClientIp(request));

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("[HTTP_ERROR] {} {} - Error: {}",
                    request.getMethod(),
                    request.getRequestURI(),
                    e.getMessage());
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();

            if (status >= 500) {
                log.error("[HTTP_RESPONSE] {} {} - {} - {}ms",
                        request.getMethod(),
                        request.getRequestURI(),
                        status,
                        duration);
            } else if (status >= 400) {
                log.warn("[HTTP_RESPONSE] {} {} - {} - {}ms",
                        request.getMethod(),
                        request.getRequestURI(),
                        status,
                        duration);
            } else {
                log.info("[HTTP_RESPONSE] {} {} - {} - {}ms",
                        request.getMethod(),
                        request.getRequestURI(),
                        status,
                        duration);
            }

            MDC.clear();
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/actuator") ||
               path.startsWith("/h2-console") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs");
    }
}