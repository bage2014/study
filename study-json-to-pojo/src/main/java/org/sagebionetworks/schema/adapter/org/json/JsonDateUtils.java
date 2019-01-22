package org.sagebionetworks.schema.adapter.org.json;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.sagebionetworks.schema.FORMAT;

/**
 * Utilities for working with dates.
 * @author jmhill
 *
 */
public class JsonDateUtils {
	
	/**
	 * Convert a date to a string of the given format.
	 * @param format
	 * @param toFormat
	 * @return
	 */
	public static String convertDateToString(FORMAT format, Date toFormat) {
		if(format == null) throw new IllegalArgumentException("FORMAT cannot be null");
		if(toFormat == null) throw new IllegalArgumentException("Date cannot be null");
		if(!format.isDateFormat()) throw new IllegalArgumentException("Not a date format: "+format.name());
		if(FORMAT.DATE_TIME == format){
			DateTime dt = new DateTime(toFormat.getTime());
			return ISODateTimeFormat.dateTime().print(dt);
		}else if(FORMAT.DATE == format){
			DateTime dt = new DateTime(toFormat.getTime());
			return ISODateTimeFormat.date().print(dt);
		}else if(FORMAT.TIME == format){
			DateTime dt = new DateTime(toFormat.getTime());
			return ISODateTimeFormat.time().print(dt);
		}else if(FORMAT.UTC_MILLISEC == format){
			return ""+toFormat.getTime();
		}else{
			throw new IllegalArgumentException("Unknown date format: "+format.name());
		}
	}

	/**
	 * Convert a string to a date of the given format.
	 * @param format
	 * @param toFormat
	 * @return
	 */
	public static Date convertStringToDate(FORMAT format, String toFormat) {
		if(format == null) throw new IllegalArgumentException("FORMAT cannot be null");
		if(toFormat == null) throw new IllegalArgumentException("Date cannot be null");
		if(!format.isDateFormat()) throw new IllegalArgumentException("Not a date format: "+format.name());
		if(FORMAT.DATE_TIME == format){
			DateTime dt = ISODateTimeFormat.dateTime().parseDateTime(toFormat);
			return dt.toDate();
		}else if(FORMAT.DATE == format){
			DateTime dt = ISODateTimeFormat.date().parseDateTime(toFormat);
			return dt.toDate();
		}else if(FORMAT.TIME == format){
			DateTime dt = ISODateTimeFormat.time().parseDateTime(toFormat);
			return dt.toDate();
		}else if(FORMAT.UTC_MILLISEC == format){
			long time = Long.parseLong(toFormat);
			return new Date(time);
		}else{
			throw new IllegalArgumentException("Unknown date format: "+format.name());
		}
	}

}
