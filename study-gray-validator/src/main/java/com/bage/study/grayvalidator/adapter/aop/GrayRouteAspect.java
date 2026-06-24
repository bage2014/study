package com.bage.study.grayvalidator.adapter.aop;

import com.bage.study.grayvalidator.core.ForwardingEngine;
import com.bage.study.grayvalidator.core.WhitelistMatcher;
import com.bage.study.grayvalidator.infra.CachedBodyRequestWrapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Aspect
public class GrayRouteAspect {

    private static final Logger log = LoggerFactory.getLogger(GrayRouteAspect.class);

    private final WhitelistMatcher matcher;
    private final ForwardingEngine engine;

    public GrayRouteAspect(WhitelistMatcher matcher, ForwardingEngine engine) {
        this.matcher = matcher;
        this.engine  = engine;
    }

    @Around("@annotation(com.bage.study.grayvalidator.adapter.aop.GrayRoute)")
    public Object route(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = currentRequest();
        if (request == null) return pjp.proceed();

        HttpServletRequest wrapped = request instanceof CachedBodyRequestWrapper
                ? request : new CachedBodyRequestWrapper(request);

        Optional<String> target = matcher.match(wrapped);
        if (target.isPresent()) {
            HttpServletResponse response = currentResponse();
            if (response != null) {
                try {
                    engine.forward(wrapped, response, target.get());
                } catch (Exception e) {
                    log.error("GrayRouteAspect forward error, target={}", target.get(), e);
                    if (!response.isCommitted()) {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                }
                return null;
            }
        }
        return pjp.proceed();
    }

    private HttpServletRequest currentRequest() {
        ServletRequestAttributes attrs = attrs();
        return attrs != null ? attrs.getRequest() : null;
    }

    private HttpServletResponse currentResponse() {
        ServletRequestAttributes attrs = attrs();
        return attrs != null ? attrs.getResponse() : null;
    }

    private ServletRequestAttributes attrs() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }
}