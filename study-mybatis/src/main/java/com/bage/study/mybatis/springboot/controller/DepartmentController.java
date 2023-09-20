package com.bage.study.mybatis.springboot.controller;

import com.bage.study.mybatis.common.dao.DepartmentMapper;
import com.bage.study.mybatis.common.domain.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	DepartmentMapper departmentMapper;
	
	@RequestMapping("/all")
	@ResponseBody
	public List<Department> getUser() {
		return departmentMapper.queryAll();
	}
}
