package com.bage.study.spring.boot.wrapper;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ResponseWrapperFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, responseWrapper);

        // 可以在这里处理响应数据
        byte[] body = responseWrapper.getContentAsByteArray();
        // 处理body，例如添加签名
        responseWrapper.setHeader("X-Signature", "some-signature");

        // 必须调用此方法以将响应数据发送到客户端
        responseWrapper.copyBodyToResponse();
    }
}
