package org.sagebionetworks.schema.adapter;

import java.util.Date;
import java.util.Map;
import java.util.Set;



public interface JSONMapAdapter extends JSONAdapter{
	
    /**
     * Get the boolean value associated with an index.
     * The string values "true" and "false" are converted to boolean.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      The truth.
     * @throws JSONObjectAdapterException If there is no value for the index or if the
     *  value is not convertible to boolean.
     */
	public boolean getBoolean(Object key) throws JSONObjectAdapterException;
    
    /**
     * Get the double value associated with an index.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      The value.
     * @throws   JSONObjectAdapterException If the key is not found or if the value cannot
     *  be converted to a number.
     */
	public double getDouble(Object key) throws JSONObjectAdapterException;


    /**
     * Get the int value associated with an index.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      The value.
     * @throws   JSONObjectAdapterException If the key is not found or if the value cannot
     *  be converted to a number.
     *  if the value cannot be converted to a number.
     */
	public int getInt(Object key) throws JSONObjectAdapterException;

    /**
     * Get a date for the given index.
     * @param index
     * @return
     * @throws JSONObjectAdapterException
     */
	public Date getDate(Object key) throws JSONObjectAdapterException;
    
    /**
     * Get the binary value.
     * @param index
     * @return
     * @throws JSONObjectAdapterException
     */
	public byte[] getBinary(Object key) throws JSONObjectAdapterException;

    /**
     * Get the JSONArray associated with an index.
     * @param index The index must be between 0 and length() - 1.
     * @return      A JSONArray value.
     * @throws JSONObjectAdapterException If there is no value for the index. or if the
     * value is not a JSONArray
     */
	public JSONArrayAdapter getJSONArray(Object key) throws JSONObjectAdapterException;
    
    
    /**
     * Get the JSONObjectAdapter associated with an index.
     * @param index
     * @return
     * @throws JSONObjectAdapterException
     */
	public JSONObjectAdapter getJSONObject(Object key) throws JSONObjectAdapterException;
    
	public Object get(Object key) throws JSONObjectAdapterException;

    /**
     * 
     * @param index
     * @param value
     * @return
     * @throws JSONObjectAdapterException
     */
	public JSONMapAdapter put(Object key, JSONArrayAdapter value) throws JSONObjectAdapterException;
    
	public JSONMapAdapter put(Object key, JSONObjectAdapter value) throws JSONObjectAdapterException;
    
	public JSONMapAdapter put(Object key, String value) throws JSONObjectAdapterException;
    
	public JSONMapAdapter put(Object key, long value) throws JSONObjectAdapterException;
    
	public JSONMapAdapter put(Object key, double value) throws JSONObjectAdapterException;
    
	public JSONMapAdapter put(Object key, boolean value) throws JSONObjectAdapterException;
    
	public JSONMapAdapter put(Object key, int value) throws JSONObjectAdapterException;
    
    /**
     * Dates put here will be FORMAT.UTC_MILLISEC
     * @param index
     * @param value
     * @return
     * @throws JSONObjectAdapterException
     */
	public JSONMapAdapter put(Object key, Date value) throws JSONObjectAdapterException;
    
	public JSONMapAdapter put(Object key, byte[] value) throws JSONObjectAdapterException;

	public JSONMapAdapter putNull(Object key) throws JSONObjectAdapterException;
    /**
     * Get the JSONObject associated with an index.
     * @param index subscript
     * @return      A JSONObject value.
     * @throws JSONOjbectAdaptorException If there is no value for the index or if the
     * value is not a JSONObject
     */


    /**
     * Get the long value associated with an index.
     *
     * @param index The index must be between 0 and length() - 1.
     * @return      The value.
     * @throws   JSONObjectAdapterException If the key is not found or if the value cannot
     *  be converted to a number.
     */
	public long getLong(Object key) throws JSONObjectAdapterException;
    /**
     * Get the string associated with an index.
     * @param index The index must be between 0 and length() - 1.
     * @return      A string value.
     * @throws JSONObjectAdapterException If there is no value for the index.
     */
	public String getString(Object key) throws JSONObjectAdapterException;

    /**
     * Determine if the value is null.
     * @param index The index must be between 0 and length() - 1.
     * @return true if the value at the index is null, or if there is no value.
     */
	public boolean isNull(Object key);
	
    /**
     * Get the number of elements in the JSONArray, included nulls.
     *
     * @return The length (or size).
     */
    public int length() ;
    
    /**
     * Convert the array to a JSON String
     * @return
     */
    public String toJSONString();

	/**
	 * get the keys for iterating over
	 * 
	 * @return
	 */
	public Iterable<Object> keys();
}
