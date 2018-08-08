package com.bage.domain;

import java.util.List;

/**
 * 权限结果
 * @author luruihua
 *
 */
public class LimitsResult {
	
	private Resource resource; // 资源
	private List<Permission> permissions;	
	
	public static LimitsResult failure() {
		// TODO Auto-generated method stub
		return null;
	}


	public LimitsResult noPermission() {
		// TODO Auto-generated method stub
		return null;
	} 
	
	// 其他信息...
	
}
