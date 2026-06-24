package com.bage.study.grayvalidator.adapter.gateway;

import com.bage.study.grayvalidator.core.ForwardingEngine;
import com.bage.study.grayvalidator.core.WhitelistMatcher;
import com.bage.study.grayvalidator.core.config.GrayTargetConfig;
import com.bage.study.grayvalidator.infra.CachedBodyRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class GrayProxyFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(GrayProxyFilter.class);

    private final WhitelistMatcher matcher;
    private final ForwardingEngine engine;
    private final GrayTargetConfig config;

    public GrayProxyFilter(WhitelistMatcher matcher, ForwardingEngine engine, GrayTargetConfig config) {
        this.matcher = matcher;
        this.engine  = engine;
        this.config  = config;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  httpReq = (HttpServletRequest)  req;
        HttpServletResponse httpRes = (HttpServletResponse) res;

        CachedBodyRequestWrapper wrapped = new CachedBodyRequestWrapper(httpReq);
        Optional<String> target = matcher.match(wrapped);
        String targetUrl = target.orElse(config.getOriginalTargetUrl());

        try {
            engine.forward(wrapped, httpRes, targetUrl);
        } catch (Exception e) {
            log.error("GrayProxyFilter unexpected error, target={}", targetUrl, e);
            if (!httpRes.isCommitted()) {
                httpRes.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}