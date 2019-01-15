package com.bage.web.reqres;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        System.out.println("MyFilter.doFilter is work....");


        chain.doFilter(new MyRequest((HttpServletRequest) request),new MyResponse((HttpServletResponse) response));
    }

    public void destroy() {

    }
}
