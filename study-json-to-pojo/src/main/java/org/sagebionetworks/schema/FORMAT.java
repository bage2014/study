package org.sagebionetworks.schema;

/**
 * 5.23. format
 * 
 * 
 * This property defines the type of data, content type, or microformat to be
 * expected in the instance property values. A format attribute MAY be one of
 * the values listed below, and if so, SHOULD adhere to the semantics describing
 * for the format. A format SHOULD only be used to give meaning to primitive
 * types (string, integer, number, or boolean). Validators MAY (but are not
 * required to) validate that the instance values conform to a format. The
 * following formats are predefined:
 * 
 * @see <a
 *      href="http://tools.ietf.org/html/draft-zyp-json-schema-03#section-6.2">http://tools.ietf.org/html/draft-zyp-json-schema-03#section-6.2</a>
 * 
 */
public enum FORMAT {

	/*
	 * This SHOULD be a date in ISO 8601 format of YYYY-MM- DDThh:mm:ssZ in UTC
	 * time. This is the recommended form of date/ timestamp.
	 */
	DATE_TIME("date-time", true),
	/*
	 * This SHOULD be a date in the format of YYYY-MM-DD. It is recommended that
	 * you use the "date-time" format instead of "date" unless you need to
	 * transfer only the date part.
	 */
	DATE("date", true),
	/*
	 * This SHOULD be a time in the format of hh:mm:ss. It is recommended that
	 * you use the "date-time" format instead of "time" unless you need to
	 * transfer only the time part.
	 */
	TIME("time", true),
	/*
	 * This SHOULD be the difference, measured in milliseconds, between the
	 * specified time and midnight, 00:00 of January 1, 1970 UTC. The value
	 * SHOULD be a number (integer or float).
	 */
	UTC_MILLISEC("utc-millisec", true),
	/*
	 * A regular expression, following the regular expression specification from
	 * ECMA 262/Perl 5.
	 */
	REGEX("regex", false),
	/*
	 * This is a CSS color (like "#FF0000" or "red"), based on CSS 2.1
	 * [W3C.CR-CSS21-20070719].
	 */
	COLOR("color", false),
	/*
	 * This is a CSS style definition (like "color: red; background-
	 * color:#FFF"), based on CSS 2.1 [W3C.CR-CSS21-20070719].
	 */
	STYLE("style", false),
	/*
	 * This SHOULD be a phone number (format MAY follow E.123)
	 */
	PHONE("phone", false),
	/*
	 * This value SHOULD be a URI..
	 */
	URI("uri", false),
	/*
	 * This SHOULD be an email address.
	 */
	EMAIL("email", false),
	/*
	 * This SHOULD be an ip version 4 address.
	 */
	IP_ADDRESS("ip-address", false),
	/*
	 * This SHOULD be an ip version 6 address.
	 */
	IPV6("ipv6", false),
	/*
	 * This SHOULD be a host-name.
	 */
	HOST_NAME("host-name", false);	
	

	/*
	 * The json value used to describe this format
	 */
	private String jsonValue;
	private boolean isDateFormat;

	private FORMAT(String jsonValue, boolean isDateFormat) {
		this.jsonValue = jsonValue;
		this.isDateFormat = isDateFormat;
	}
	
	/**
	 * Is this a date format?
	 * @return
	 */
	public boolean isDateFormat(){
		return this.isDateFormat;
	}
	
	public static FORMAT getFormatForJSONValue(String JSONValue){
		for(FORMAT format: FORMAT.values()){
			if(format.jsonValue.equals(JSONValue)) return format;
		}
		throw new IllegalArgumentException("Cannot match FORMAT to JSON value: "+JSONValue);
	}

	public String getJSONValue() {
		return this.jsonValue;
	}

}
