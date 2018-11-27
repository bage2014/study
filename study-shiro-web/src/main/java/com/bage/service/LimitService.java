package com.bage.service;

import java.util.ArrayList;
import java.util.List;

import com.bage.domain.LimitsResult;
import com.bage.domain.base.Operation;
import com.bage.domain.base.Resource;
import com.bage.domain.base.User;
import com.bage.domain.param.DataParam;

public class LimitService {

	public List<LimitsResult> query(User user, List<Resource> resources, DataParam dataDom) {
		List<LimitsResult> list = new ArrayList<LimitsResult>();
		return null;
	}

	public boolean verify(User user, Operation operation, List<Resource> resources, DataParam dataDom) {
		// TODO Auto-generated method stub
		return false;
	}

	public List<LimitsResult> query(User user, Resource resource, DataParam dataDom) {
		// TODO Auto-generated method stub
		return null;
	}

}
