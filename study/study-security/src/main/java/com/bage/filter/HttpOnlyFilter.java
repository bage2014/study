package com.bage.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class HttpOnlyFilter implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 设置httponly
		Cookie[] cookies = ((HttpServletRequest)request).getCookies();
		if(cookies != null) {
			for ( Cookie coki : cookies) {
				coki.setHttpOnly(true);
			}
		}
		chain.doFilter(request, response);
	}

	public void destroy() {
		
	}

}
