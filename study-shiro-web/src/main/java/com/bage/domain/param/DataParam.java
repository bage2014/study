package com.bage.domain.param;

/**
 * 权限数据参数
 * @author luruihua
 *
 */
public class DataParam {

	private String id; // 主键值
	private String pkField; // 主键列名
	private String table; // 所属表
	
	public DataParam() {
		super();
	}

	public DataParam(String id, String pkField, String table) {
		super();
		this.id = id;
		this.pkField = pkField;
		this.table = table;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPkField() {
		return pkField;
	}
	public void setPkField(String pkField) {
		this.pkField = pkField;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((pkField == null) ? 0 : pkField.hashCode());
		result = prime * result + ((table == null) ? 0 : table.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DataParam other = (DataParam) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (pkField == null) {
			if (other.pkField != null)
				return false;
		} else if (!pkField.equals(other.pkField))
			return false;
		if (table == null) {
			if (other.table != null)
				return false;
		} else if (!table.equals(other.table))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DataParam [id=" + id + ", pkField=" + pkField + ", table=" + table + "]";
	}
	
	
	
}
