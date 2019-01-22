package org.sagebionetworks.schema;

/**
 * 
 * Used to indicate how binary data is encode.
 * 
 * These encoding types are defined in http://tools.ietf.org/html/rfc2045#section-6.1
 * 
 * @author jmhill
 *
 */
public enum ENCODING {

	/*
	 * There is not transformation applied to this string. Rather this indicates that
	 * that the data is a long string.
	 */
	BINARY("binary");
	
	/*
	 * The json value used to describe this format
	 */
	private String jsonValue;
	
	private ENCODING(String jsonString){
		this.jsonValue = jsonString;
	}
	
	/**
	 * The value used write to JSON
	 * @return
	 */
	public String getJsonValue(){
		return this.jsonValue;
	}
	
	/**
	 * 
	 * @param JSONValue
	 * @return
	 */
	public static ENCODING getEncodingForJSONValue(String JSONValue){
		for(ENCODING encoding: ENCODING.values()){
			if(encoding.jsonValue.equals(JSONValue)) return encoding;
		}
		throw new IllegalArgumentException("Cannot match ENCODING to JSON value: "+JSONValue);
	}
}
