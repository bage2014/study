package com.bage.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bage.constant.Constants;
import com.bage.utils.JWTUtils;
import com.bage.utils.RedisUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@WebFilter(urlPatterns="/*")
public class SessionFilter implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = ((HttpServletRequest)req);
		HttpServletResponse response = ((HttpServletResponse)res);

		if(!isExcludeUri(request)) {
			Object account = request.getSession().getAttribute(Constants.session_attribute_currentuser);
			if(account == null) {
				response.sendRedirect(request.getServletContext().getContextPath() + "/");
				return ;
			}
			
			String compactJws = request.getParameter("compactJws");
			// 签名验证
			try {
				
				Jws<Claims> jws = JWTUtils.parse(compactJws );
				String sub = jws.getBody().getSubject();
				String currentCompactJws = RedisUtils.getString(sub);
				if(currentCompactJws == null || !currentCompactJws.equals(compactJws)) {
					System.out.println("签名不合法:\ncompactJws：" + compactJws);
					System.out.println("currentCompactJws：" + currentCompactJws);
					response.sendRedirect(request.getServletContext().getContextPath() + "/");
					return ;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("解析签名报错");
				System.out.println("compactJws：" + compactJws);
				response.sendRedirect(request.getServletContext().getContextPath() + "/");
				return ;
			}
		}
		
		chain.doFilter(request, response);
	}

	private boolean isExcludeUri(HttpServletRequest request) {
		String uri = request.getRequestURI();
		if((request.getServletContext().getContextPath() + "/").equals(uri)) { // 首页不拦截
			return true;
		}
		if((request.getServletContext().getContextPath() + "/user/login").equals(uri)) { // 登录页面不拦截
			return true;
		}
		return false;
	}

	public void destroy() {
		
	}

	
}
