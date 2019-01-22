package org.sagebionetworks.schema;

/**
 * Abstraction for a class with direct access to its effective schema.
 *
 */
public interface HasEffectiveSchema {

	/**
	 * Get the effective schema for this class.
	 * 
	 * @return
	 */
	public String getEffectiveSchema();
}
