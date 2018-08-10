package com.bage.domain;

import java.util.List;

import com.bage.domain.base.Resource;

/**
 * 权限结果
 * @author luruihua
 *
 */
public class LimitsResult {
	
	private List<Resource> resources; //菜单
	
	public LimitsResult() {
		super();
	}

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public static LimitsResult failure() {
		return null;
	}

	public LimitsResult noPermission() {
		return null;
	} 
	
}
