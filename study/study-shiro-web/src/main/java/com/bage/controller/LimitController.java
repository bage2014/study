package com.bage.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bage.domain.base.Resource;
import com.bage.domain.base.User;
import com.bage.domain.param.DataParam;
import com.bage.service.LimitService;
import com.bage.utils.JsonUtils;
import com.bage.utils.ParamUtils;

/**
 * 权限控制器（暂时省略所有注解）
 * @author luruihua
 *
 */
public class LimitController {

	LimitService limitService;
	
	// 查询所有资源的权限
	public String query(HttpServletRequest request,HttpServletResponse response) {
		// 获取当前操作用户信息
		User user = ParamUtils.getUser(request);
		// 解析资源
		List<Resource> resources= ParamUtils.getResources(request);
		// 解析数据
		DataParam dataDom = ParamUtils.getLimitData(request);
		// 校验操作用户当前的操作是否具有此操作权限
		return JsonUtils.toJson(limitService.query(user,resources,dataDom));
	}
	
	// 查询所有操作按钮权限
	public String queryLimit(HttpServletRequest request,HttpServletResponse response) {
		// 获取当前操作用户信息
		User user = ParamUtils.getUser(request);
		// 解析资源
		Resource resource = ParamUtils.getResource(request);
		// 解析数据
		DataParam dataDom = ParamUtils.getLimitData(request);
		// 校验操作用户当前的操作是否具有此操作权限
		return JsonUtils.toJson(limitService.query(user,resource,dataDom));
	}
	
}
