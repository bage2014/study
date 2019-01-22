package org.sagebionetworks.schema;

import java.util.Map;

/**
 * This attribute defines what the primitive type or the schema of the instance
 * MUST be in order to validate. This attribute can take one of two forms:
 * 
 * @see <a
 *      href="http://tools.ietf.org/html/draft-zyp-json-schema-03#section-6.2">http://tools.ietf.org/html/draft-zyp-json-schema-03#section-6.2</a>
 * @author jmhill
 * 
 */
public enum TYPE {
	/*
	 * Value MUST be a string.
	 */
	STRING("string", false, String.class.getName(), "getString"),
	/*
	 * Value MUST be a number, floating point numbers are allowed.
	 */
	NUMBER("number", false, Double.class.getName(), "getDouble"),
	/*
	 * Value MUST be an integer, no floating point numbers are allowed. This is
	 * a subset of the number type.
	 */
	INTEGER("integer", false, Long.class.getName(), "getLong"),
	/*
	 * Value MUST be a boolean.
	 */
	BOOLEAN("boolean", false, Boolean.class.getName(), "getBoolean"),
	/*
	 * Value MUST be an object.
	 */
	OBJECT("object", false, Object.class.getName(), "getJSONObject"),
	/*
	 * Value MUST be an array.
	 */
	ARRAY("array", false, "[]", "getJSONArray"),
	/*
	 * Value MUST be a map
	 */
	MAP("map", false, Map.class.getName(), "getJSONMap()"),
	/*
	 * Value MUST be null. Note this is mainly for purpose of being able use
	 * union types to define nullability. If this type is not included in a
	 * union, null values are not allowed (the primitives listed above do not
	 * allow nulls on their own).
	 */
	NULL("null", false, "NULL", "getJSONObject"),
	/*
	 * Value MAY be of any type including null.
	 */
	ANY("any", false, "any", "getJSONObject"),
	
	/*
	 * This is a custom type used to indicate the schema describes an interface. 
	 */
	INTERFACE("interface", false, "Interface", null),
	;

	/*
	 * The value that this type is as it appears in the JSON.
	 */
	private String jsonValue;
	private String javaType;
	private boolean isPrimitive;
	private String getMethodName;

	private TYPE(String jsonValue, boolean isPrimitive, String javaType, String getMethod) {
		this.jsonValue = jsonValue;
		this.javaType = javaType;
		this.isPrimitive = isPrimitive;
		this.getMethodName = getMethod;
	}

	/**
	 * Get the JSON value for this object.
	 * @return
	 */
	public String getJSONValue() {
		return this.jsonValue;
	}
	
	/**
	 * Get the Type from the JSON Value
	 * @param jsonValue
	 * @return
	 */
	public static TYPE getTypeFromJSONValue(String jsonValue){
		for(TYPE type: TYPE.values()){
			if(type.jsonValue.equals(jsonValue)) return type;
		}
		throw new IllegalArgumentException("Unknown JSON Value: "+jsonValue);
	}
	/**
	 * Get the java type
	 * @return
	 */
	public String getJavaType(){
		return this.javaType;
	}
	
	/**
	 * Is this a java primitive type?
	 * @return
	 */
	public boolean isPrimitive(){
		return this.isPrimitive;
	}

	/**
	 * The name of the getter method for the adapter for this type.
	 * @return
	 */
	public String getMethodName(){
		return this.getMethodName;
	}
}