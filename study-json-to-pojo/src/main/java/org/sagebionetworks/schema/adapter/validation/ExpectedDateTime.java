package org.sagebionetworks.schema.adapter.validation;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.sagebionetworks.schema.FORMAT;

/**
 * This can be used to validate String to Date conversions.
 * @author jmhill
 *
 */
public class ExpectedDateTime {
	
	private static final Map<String, Date> expectedDateTime;
	private static final Map<String, Date> expectedDate;
	private static final Map<String, Date> expectedTime;
	
	static{
		// Build the Date-time
		Map<String, Date> map = new HashMap<String, Date>();
		// This is the date in PAC time
		map.put("1925-12-06T22:59:48.989-07:00", new Date(-1390672811011l));
		// The same date as above but in UTC
		map.put("1925-12-07T05:59:48.989Z", new Date(-1390672811011l));
		
		map.put("2011-01-29T00:00:00.501-07:00", new Date(1296284400501l));
		map.put("2011-01-29T07:00:00.501Z", new Date(1296284400501l));
		expectedDateTime = Collections.unmodifiableMap(map);
		
		// Build the date
		map = new HashMap<String, Date>();
		// This is the date in PAC time
//		map.put("2011-01-29", new Date(1296288000000l));
		expectedDate = Collections.unmodifiableMap(map);
		
		// Build the time
		map = new HashMap<String, Date>();
		// This is the date in PAC time
		// The same date as above but in UTC
//		map.put("12:00:00.501Z", new Date(43200501));
		expectedTime = Collections.unmodifiableMap(map);
		
	}
	
	/**
	 * All of the expected DateTime Strings to test.
	 * @return
	 */
	public static Iterator<String> getExpectedFormatedString(FORMAT format){
		if(format == null) throw new IllegalArgumentException("FORMAT cannot be null");
		return getMapForFormat(format).keySet().iterator();
	}
	
	/**
	 * The map for the given format.
	 * 
	 * @param format
	 * @return
	 * @throws IllegalArgumentException if the passed FORMAT is not a supported date format.
	 */
	private static Map<String, Date> getMapForFormat(FORMAT format){
		if(format == null) throw new IllegalArgumentException("FORMAT cannot be null");
		if(FORMAT.DATE_TIME == format) {
			return expectedDateTime;
		}else if(FORMAT.DATE == format){
			return expectedDate;
		}else if(FORMAT.TIME == format){
			return expectedTime;
		}else{
			throw new IllegalArgumentException("Unknown Date FORMAT: "+format);
		}
	}
	
	/**
	 * Get the expected date for a given string and format.
	 * @param dateTimeString
	 * @return
	 */
	public static Date getExpectedDateForString(FORMAT format, String dateTimeString){
		return getMapForFormat(format).get(dateTimeString);
	}
	
	/**
	 * Is this a supported date format?
	 * @param format
	 * @return
	 */
	public static boolean isSupportedFormat(FORMAT format){
		try{
			getMapForFormat(format);
			return true;
		}catch (IllegalArgumentException e){
			return false;
		}
	}

}
