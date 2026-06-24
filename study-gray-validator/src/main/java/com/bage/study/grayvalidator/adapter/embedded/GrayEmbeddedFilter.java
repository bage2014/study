package com.bage.study.grayvalidator.adapter.embedded;

import com.bage.study.grayvalidator.core.ForwardingEngine;
import com.bage.study.grayvalidator.core.WhitelistMatcher;
import com.bage.study.grayvalidator.infra.CachedBodyRequestWrapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

public class GrayEmbeddedFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(GrayEmbeddedFilter.class);

    private final WhitelistMatcher matcher;
    private final ForwardingEngine engine;

    public GrayEmbeddedFilter(WhitelistMatcher matcher, ForwardingEngine engine) {
        this.matcher = matcher;
        this.engine  = engine;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  httpReq = (HttpServletRequest)  req;
        HttpServletResponse httpRes = (HttpServletResponse) res;

        CachedBodyRequestWrapper wrapped = new CachedBodyRequestWrapper(httpReq);
        Optional<String> target = matcher.match(wrapped);

        if (target.isEmpty()) {
            chain.doFilter(wrapped, res);
            return;
        }

        try {
            engine.forward(wrapped, httpRes, target.get());
        } catch (Exception e) {
            log.error("GrayEmbeddedFilter forward error, target={}", target.get(), e);
            if (!httpRes.isCommitted()) {
                httpRes.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}