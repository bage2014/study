package org.sagebionetworks.schema;

import org.sagebionetworks.schema.adapter.JSONEntity;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;

/**
 * Represents a single enumeration values.
 *
 */
public class EnumValue implements JSONEntity {
	
	public static final String JSON_NAME = "name";
	public static final String JSON_DESCRIPTION = "description";
	
	private String name;
	private String description;
	
	public EnumValue() {
	}
	
	
	/**
	 * 
	 * @param name Name of the enumeration value.
	 */
	public EnumValue(String name) {
		super();
		this.name = name;
	}

	/**
	 * 
	 * @param name Name of the enumeration value.
	 * @param description Description of the enumeration value.
	 */
	public EnumValue(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	/**
	 * Name of the enumeration value.
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Name of the enumeration value.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Description of the enumeration value.
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Description of the enumeration value
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public JSONObjectAdapter initializeFromJSONObject(JSONObjectAdapter toInitFrom) throws JSONObjectAdapterException {
		if(toInitFrom.has(JSON_NAME)) {
			this.name = toInitFrom.getString(JSON_NAME);
		}
		if(toInitFrom.has(JSON_DESCRIPTION)) {
			this.description = toInitFrom.getString(JSON_DESCRIPTION);
		}
		return toInitFrom;
	}

	@Override
	public JSONObjectAdapter writeToJSONObject(JSONObjectAdapter writeTo) throws JSONObjectAdapterException {
		if(name != null) {
			writeTo.put(JSON_NAME, this.name);
		}
		if(description != null) {
			writeTo.put(JSON_DESCRIPTION, this.description);
		}
		return writeTo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		EnumValue other = (EnumValue) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "EnumValue [name=" + name + ", description=" + description + "]";
	}

}
