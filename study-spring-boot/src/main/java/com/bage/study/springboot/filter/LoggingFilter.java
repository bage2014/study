package com.bage.study.springboot.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(2)
public class LoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request,ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        System.out.println(String.format("Logging Request  %s : %s", req.getMethod(),
                req.getRequestURI()));
        chain.doFilter(request, response);
        System.out.println(String.format("Logging Response :%s",res.getContentType()));
    }

    @Override
    public void destroy() {

    }

    // other methods
}

