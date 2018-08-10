package com.bage.domain;

public class Button {
	
	private String id; // ID
	private String code; // 编码
	private String name; // 名称
	private String desc; // 描述

	public Button() {
		super();
	}
	
	public Button(String id, String code, String name, String desc) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.desc = desc;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "OperationButton [id=" + id + ", code=" + code + ", name=" + name + ", desc=" + desc + "]";
	}
	
}
