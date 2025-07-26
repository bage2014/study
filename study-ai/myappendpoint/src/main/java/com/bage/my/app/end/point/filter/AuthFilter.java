package com.bage.my.app.end.point.filter;

import com.bage.my.app.end.point.config.AuthConfig;
import com.bage.my.app.end.point.entity.UserToken;
import com.bage.my.app.end.point.repository.UserTokenRepository;
import com.bage.my.app.end.point.dto.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
public class AuthFilter implements Filter {
    @Autowired
    private UserTokenRepository userTokenRepository;

    @Autowired
    private AuthConfig authConfig;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String url = request.getRequestURI();
        log.debug("Request URL: {}", url);

        // 检查是否是不拦截的URL
        if (isExcludedUrl(url)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 尝试获取Authorization头
        String authorization = request.getHeader("Authorization");

        // 清除之前可能存在的用户上下文
        UserContext.clear();

        if (authorization != null && !authorization.isEmpty()) {
            try {
                // 校验token
                UserToken userToken = userTokenRepository.findByToken(authorization);
                if (userToken != null) {
                    // 检查token是否过期
                    if (userToken.getTokenExpireTime().isAfter(LocalDateTime.now())) {
                        // 登录成功，设置用户上下文
                        UserContext.setUserId(userToken.getUserId());
                        log.debug("User {} logged in successfully", userToken.getUserId());

                        // 继续处理请求
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
            } catch (Exception e) {
                log.error("Error during authentication: {}", e.getMessage());
            }
        }

        // 认证失败，返回401错误
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write("Authentication failed");
        response.getWriter().flush();
    }

    /**
     * 检查URL是否是不拦截的URL
     */
    private boolean isExcludedUrl(String url) {
        for (String pattern : authConfig.getExcludedUrlPatterns()) {
            if (pathMatcher.match(pattern, url)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化方法
    }

    @Override
    public void destroy() {
        // 销毁方法
    }
}