package org.sagebionetworks.schema.adapter;

import java.util.Date;

import org.sagebionetworks.schema.FORMAT;

/**
 * Abstraction for all adapters.
 * Adapters must also act as adapter factories
 * @author John
 *
 */
public interface JSONAdapter extends AdapterFactory {
	
	/**
	 * Write this object to its JSON string.
	 * @return
	 */
	public String toJSONString();
	
	/**
	 * Convert a Date to a string of the given format.
	 * @param format
	 * @param toFormat
	 * @return
	 */
	public String convertDateToString(FORMAT format, Date toFormat);
	
	/**
	 * Convert a String to a Date of the given format.
	 * @param format
	 * @param toFormat
	 * @return
	 */
	public Date convertStringToDate(FORMAT format, String toFormat);
}
