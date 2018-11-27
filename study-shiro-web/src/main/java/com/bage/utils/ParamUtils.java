package com.bage.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletRequest;

import com.bage.constant.Constants;
import com.bage.domain.base.Operation;
import com.bage.domain.base.Resource;
import com.bage.domain.base.User;
import com.bage.domain.param.DataParam;

public class ParamUtils {

	public static DataParam getLimitData(ServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<Resource> getResources(ServletRequest request) {
		String resourcesString = request.getParameter(Constants.param_key_limit_resources);
		List<Resource> resources = new ArrayList<Resource>();
		try {
			resources = toList(resourcesString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resources;
	}
	public static Resource getResource(ServletRequest request) {
		List<Resource> resources = getResources(request);
		if(resources == null || resources.isEmpty()) {
			return null;
		}
		return resources.get(0);
	}
	@SuppressWarnings("unchecked")
	private static List<Resource> toList(String resourcesString) {
		if(resourcesString == null) {
			return new ArrayList<Resource>();
		}
		return (List<Resource>)JsonUtils.fromJson(resourcesString,List.class);
	}

	public static User getUser(ServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Operation getOperation(ServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
