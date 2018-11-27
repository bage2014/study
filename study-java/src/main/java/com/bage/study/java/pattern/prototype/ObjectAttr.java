package com.bage.study.java.pattern.prototype;

import java.io.Serializable;

/**
 * 原型对象的子属性
 * @author bage
 *
 */
public class ObjectAttr implements Cloneable,Serializable{

	private static final long serialVersionUID = 1L;
	
	private String value;

	public ObjectAttr() {
		super();
	}
	
	public ObjectAttr(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	@Override
	public ObjectAttr clone() throws CloneNotSupportedException {
		return (ObjectAttr) super.clone();
	}
}
