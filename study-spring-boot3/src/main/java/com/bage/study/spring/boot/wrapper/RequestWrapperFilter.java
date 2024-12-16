package com.bage.study.spring.boot.wrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;


@Component
public class RequestWrapperFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        requestWrapper.setAttribute("X-Signature-request","hello-world");
        // 可以在这里处理请求数据
        byte[] body = requestWrapper.getContentAsByteArray();
        // 处理body，例如记录日志
        //。。。
        filterChain.doFilter(requestWrapper, response);
    }

}
