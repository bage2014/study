package org.sagebionetworks.schema.adapter;

/**
 * A JSONEntity can marshal itself to/from JSON using a JSONObjectAdapter
 *
 */
public interface JSONEntity {
		
	/**
	 * Fully initialize this object from a JSONObjectAdapter.
	 * @param toInitFrom
	 */
	public JSONObjectAdapter initializeFromJSONObject(JSONObjectAdapter toInitFrom) throws JSONObjectAdapterException;
	
	/**
	 * Fully write this object to a JSONObjectAdapter.
	 * @param writeTo
	 */
	public JSONObjectAdapter writeToJSONObject(JSONObjectAdapter writeTo) throws JSONObjectAdapterException;
	
}
