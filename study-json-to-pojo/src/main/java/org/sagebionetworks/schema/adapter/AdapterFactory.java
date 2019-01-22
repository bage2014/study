package org.sagebionetworks.schema.adapter;

/**
 * An abstraction for creating adapters
 * @author John
 *
 */
public interface AdapterFactory {
	
	/**
	 * Create a new Object.
	 * @return
	 */
	public JSONObjectAdapter createNew();
	
	/**
	 * Create a new Adapter using the passed JSON String
	 * @param json
	 * @return
	 * @throws JSONObjectAdapterException 
	 */
	public JSONObjectAdapter createNew(String json) throws JSONObjectAdapterException;
	
	/**
	 * Create a new array.
	 * @return
	 */
	public JSONArrayAdapter createNewArray();
	
	/**
	 * Create a new array.
	 * @return
	 */
	public JSONArrayAdapter createNewArray(String json) throws JSONObjectAdapterException;

	/**
	 * Create a new map.
	 * 
	 * @return
	 */
	public JSONMapAdapter createNewMap();

	/**
	 * Create a new map.
	 * 
	 * @return
	 */
	public JSONMapAdapter createNewMap(String json) throws JSONObjectAdapterException;

}
