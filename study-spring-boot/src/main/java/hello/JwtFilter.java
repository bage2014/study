package hello;

import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Objects;

//@WebFilter(urlPatterns = "/*")
//@Order(1)
public class JwtFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("AuthFilter request = " + servletRequest);

        String jwt = servletRequest.getParameter("jwt");
        System.out.println("param jwt = " + jwt);
        if(Objects.isNull(jwt)){
            servletResponse.getWriter().append("not login ...");
            System.out.println("jwt = " + jwt);
            return ;
        }
    }

    @Override
    public void destroy() {

    }
}
