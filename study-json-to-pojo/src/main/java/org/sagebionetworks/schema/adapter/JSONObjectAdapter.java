package org.sagebionetworks.schema.adapter;

import java.util.Date;
import java.util.Iterator;

import org.sagebionetworks.schema.FORMAT;


/**
 * There are at least two different types of JSONObjets that our clients depend on.
 * The adapter interfaces allow us to create model objects that can work with JSON
 * without depending on a specific implementation.  
 * 
 * @author jmhill
 *
 */
public interface JSONObjectAdapter extends JSONAdapter, ValidateProperty {
	
	/**
	 * Key iterator.
	 * @return
	 */
	public Iterator<String> keys(); 

    /**
     * Get the string associated with a key.
     *
     * @param key   A key string.
     * @return      A string which is the value.
     * @throws   JSONObjectAdapterException if the key is not found.
     */
	public String getString(String key) throws JSONObjectAdapterException;
	
	public Object get(String key) throws JSONObjectAdapterException;
	
	public JSONObjectAdapter put(String key, boolean value) throws JSONObjectAdapterException;
	
	public JSONObjectAdapter put(String key, String value) throws JSONObjectAdapterException;
	
	public JSONObjectAdapter put(String key, double value) throws JSONObjectAdapterException;
	
	public JSONObjectAdapter put(String key, int value) throws JSONObjectAdapterException;
	
	public JSONObjectAdapter put(String key, long value) throws JSONObjectAdapterException;	
	
	/**
	 * Dates put here will be FORMAT.UTC_MILLISEC
	 * @param key
	 * @param value
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public JSONObjectAdapter put(String key, Date value) throws JSONObjectAdapterException;	
	
	public JSONObjectAdapter put(String key, byte[] value) throws JSONObjectAdapterException;	
	
	public JSONObjectAdapter putNull(String key) throws JSONObjectAdapterException;	
	
	
	/**
	 * @param key
	 * @param value
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public JSONObjectAdapter put(String key, JSONObjectAdapter value) throws JSONObjectAdapterException;
	
	public JSONObjectAdapter put(String key, JSONArrayAdapter value) throws JSONObjectAdapterException;
	
	public JSONObjectAdapter put(String key, JSONMapAdapter value) throws JSONObjectAdapterException;
	
    /**
     * Get the long value associated with a key. If the number value is too
     * long for a long, it will be clipped.
     *
     * @param key   A key string.
     * @return      The long value.
     * @throws   JSONObjectAdapterException if the key is not found or if the value cannot
     *  be converted to a long.
     */
	public long getLong(String key) throws JSONObjectAdapterException;

    /**
     * Get the boolean value associated with a key.
     *
     * @param key   A key string.
     * @return      The truth.
     * @throws   JSONObjectAdapterException
     *  if the value is not a Boolean or the String "true" or "false".
     */
	public boolean getBoolean(String key)throws JSONObjectAdapterException;
	
    /**
     * Get the double value associated with a key.
     * @param key   A key string.
     * @return      The numeric value.
     * @throws JSONObjectAdapterException if the key is not found or
     *  if the value is not a Number object and cannot be converted to a number.
     */
    public double getDouble(String key) throws JSONObjectAdapterException;
    
    /**
     * Get the int value associated with a key. If the number value is too
     * large for an int, it will be clipped.
     *
     * @param key   A key string.
     * @return      The integer value.
     * @throws   JSONObjectAdapterException if the key is not found or if the value cannot
     *  be converted to an integer.
     */
    public int getInt(String key) throws JSONObjectAdapterException;
    
    /**
     * Get a Date
     * @param key
     * @return
     * @throws JSONObjectAdapterException 
     */
    public Date getDate(String key) throws JSONObjectAdapterException;
    
    /**
     * Get a binary string.  This should be Base64 encoded.
     * @param key
     * @return
     * @throws JSONObjectAdapterException
     */
    public byte[] getBinary(String key) throws JSONObjectAdapterException;
    
    /**
     * Get the JSONArray value associated with a key.
     *
     * @param key   A key string.
     * @return      A JSONArray which is the value.
     * @throws   JSONObjectAdapterException if the key is not found or
     *  if the value is not a JSONArray.
     */
    public JSONArrayAdapter getJSONArray(String key) throws JSONObjectAdapterException;
    
    /**
	 * Get the JSONMap value associated with a key.
	 * 
	 * @param key A key string.
	 * @return A JSONMap which is the value.
	 * @throws JSONObjectAdapterException if the key is not found or if the value is not a JSONMap.
	 */
	public JSONMapAdapter getJSONMap(String key) throws JSONObjectAdapterException;

	/**
	 * Get the JSONObject value associated with a key.
	 * 
	 * @param key A key string.
	 * @return A JSONObject which is the value.
	 * @throws JSONObjectAdapterException if the key is not found or if the value is not a JSONObject.
	 */
    public JSONObjectAdapter getJSONObject(String key) throws JSONObjectAdapterException;
	
    /**
     * Determine if the JSONObject contains a specific key.
     * @param key   A key string.
     * @return      true if the key exists in the JSONObject.
     */
	public boolean has(String key);
	
    /**
     * Determine if the value associated with the key is null or if there is
     *  no value.
     * @param key   A key string.
     * @return      true if there is no value associated with the key or if
     *  the value is the JSONObject.NULL object.
     */
	public boolean isNull(String key);
	
	/**
	 * Method to validate a regular expression string against a pattern.
	 */
	public boolean validatePatternProperty(String pattern, String property);
	
	/**
	 * 
	 * @param uri
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public boolean validateURI(String uri) throws JSONObjectAdapterException;

}
