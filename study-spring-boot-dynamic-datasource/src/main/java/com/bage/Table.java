package com.bage;

import java.io.Serializable;

public class Table implements Serializable {

	private String dbName;
	private String name;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Table{" +
				"dbName='" + dbName + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}