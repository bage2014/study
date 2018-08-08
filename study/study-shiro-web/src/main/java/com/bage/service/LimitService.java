package com.bage.service;

import java.util.ArrayList;
import java.util.List;

import com.bage.domain.LimitData;
import com.bage.domain.LimitsResult;
import com.bage.domain.Operation;
import com.bage.domain.Resource;
import com.bage.domain.User;

public class LimitService {

	public List<LimitsResult> query(User user, List<Resource> resources, LimitData dataDom) {
		List<LimitsResult> list = new ArrayList<LimitsResult>();
		list.add(null);
		return null;
	}

	public boolean verify(User user, Operation operation, List<Resource> resources, LimitData dataDom) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<LimitsResult> query(User user, Resource resource, LimitData dataDom) {
		// TODO Auto-generated method stub
		return null;
	}

}
