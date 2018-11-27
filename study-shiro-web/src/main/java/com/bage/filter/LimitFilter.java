package com.bage.filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.bage.domain.LimitsResult;
import com.bage.domain.base.Operation;
import com.bage.domain.base.Resource;
import com.bage.domain.base.User;
import com.bage.domain.param.DataParam;
import com.bage.service.LimitService;
import com.bage.utils.JsonUtils;
import com.bage.utils.ParamUtils;

/**
 * 校验操作权限
 * @author luruihua
 *
 */
public class LimitFilter implements Filter{

	LimitService limitService;
	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		
		// 获取当前操作用户信息
		User user = ParamUtils.getUser(request);
		// 解析出当前操作
		Operation operation = ParamUtils.getOperation(request);
		// 解析出当前操作的资源
		List<Resource> resources = ParamUtils.getResources(request);
		// 解析出当前操作的数据
		DataParam dataDom = ParamUtils.getLimitData(request);
		// 校验操作用户当前的操作是否具有此操作权限
		boolean isSuccess = limitService.verify(user,operation,resources,dataDom);
		if(isSuccess) {
			filterChain.doFilter(request, response);
		} else {
			output(response,LimitsResult.failure().noPermission());
			return ;
		}		
	}

	private void output(ServletResponse response, LimitsResult limitsResult) {
		try {
			response.getWriter().write(JsonUtils.toJson(limitsResult));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public void init(FilterConfig arg0) throws ServletException {
	}
	public void destroy() {
	}
}
