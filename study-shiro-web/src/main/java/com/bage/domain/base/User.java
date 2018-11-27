package com.bage.domain.base;

import java.util.Date;

/**
 * 用户信息
 * @author luruihua
 *
 */
public class User {

	private String id;
	private String name;
	private String sex;
	private Date birthday;
	
	public User() {
		super();
	}
	
	public User(String id, String name, String sex, Date birthday) {
		super();
		this.id = id;
		this.name = name;
		this.sex = sex;
		this.birthday = birthday;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", sex=" + sex + ", birthday=" + birthday + "]";
	}
	
}
