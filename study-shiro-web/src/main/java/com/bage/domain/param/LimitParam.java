package com.bage.domain.param;

import java.util.List;

import com.bage.domain.base.Resource;
import com.bage.domain.base.User;

/**
 * 权限参数
 * @author luruihua
 *
 */
public class LimitParam {
	
	private User user; // 用户信息
	private List<Resource> resources; // 资源
	private DataParam limitData; // 数据信息
	private Object bundle; // 附件信息
	
	public LimitParam() {
		super();
	}

	public LimitParam(User user, List<Resource> resources, DataParam limitData, Object bundle) {
		super();
		this.user = user;
		this.resources = resources;
		this.limitData = limitData;
		this.bundle = bundle;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Resource> getResources() {
		return resources;
	}
	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
	public DataParam getLimitData() {
		return limitData;
	}
	public void setLimitData(DataParam limitData) {
		this.limitData = limitData;
	}

	@Override
	public String toString() {
		return "LimitParam [user=" + user + ", resources=" + resources + ", limitData=" + limitData + "]";
	}
	
	// 其他信息...
	
}
