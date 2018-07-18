package com.bage.filter;

import java.io.IOException;
import java.security.Key;

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
import com.bage.utils.JwtsBuilder;
import com.bage.utils.RedisUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

@WebFilter(urlPatterns="/*")
public class JwtFilter implements Filter{

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest request = ((HttpServletRequest)req);
		HttpServletResponse response = ((HttpServletResponse)res);

		if(!isExcludeUri(request)) {
			
			String claimsJws = request.getHeader("Authorization");
			
			System.out.println("参数compactJws：" + claimsJws);
			// 签名验证
			try {
				
				Claims claims = JwtsBuilder.decodeTokenClaims(claimsJws);
				String subject = claims.getSubject();
				Key key = (Key) RedisUtils.get(Constants.redis_key_jwt + "_" + subject);
				Jws<Claims> jws = Jwts.parser().setSigningKey(key).parseClaimsJws(claimsJws);
				System.out.println("jti:" + jws.getBody().get("jti"));
				String currentCompactJws = RedisUtils.getString(Constants.redis_key_jwt + "_" + subject);
				if(currentCompactJws == null || !currentCompactJws.equals(claimsJws)) {
					System.out.println("签名不合法:\ncompactJws：" + claimsJws);
					System.out.println("currentCompactJws：" + currentCompactJws);
					checkJwtFail(request, response);
					return ;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("解析签名报错");
				checkJwtFail(request, response);
				return ;
			}
		}
		
		chain.doFilter(request, response);
	}


	private void checkJwtFail(HttpServletRequest request,HttpServletResponse response) {
		try {
			String requestType = request.getHeader("X-Requested-With");
			if("XMLHttpRequest".equals(requestType)){
			    System.out.println("AJAX请求..");
			    response.getWriter().print("Authorization Failed");
			}else{
			    System.out.println("非AJAX请求..");
			    response.sendRedirect(request.getServletContext().getContextPath() + "/");
			    //此时requestType为null
			}
			//request.getSession().removeAttribute(Constants.session_attribute_currentuser);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean isExcludeUri(HttpServletRequest request) {
		String uri = request.getRequestURI();
		System.out.println(uri);
		if(uri.toLowerCase().endsWith(".html")) { // html页面不拦截
			return true;
		}
		if(uri.toLowerCase().endsWith(".css")) { // css页面不拦截
			return true;
		}
		if(uri.toLowerCase().endsWith(".js")) { // js页面不拦截
			return true;
		}
		if((request.getServletContext().getContextPath() + "/").equals(uri)) { // 首页不拦截
			return true;
		}
		if((request.getServletContext().getContextPath() + "/user/login").equals(uri)) { // 登录页面不拦截
			return true;
		}
		if((request.getServletContext().getContextPath() + "/user/logout").equals(uri)) { // 注销页面不拦截
			return true;
		}
		return false;
	}

	public void destroy() {
		
	}

	
}
