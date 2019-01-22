package org.sagebionetworks.schema.adapter;

/**
 * Interface for all adapters to use to validate pattern properties.  
 * @author ntiedema
 *
 */
public interface ValidateProperty {
	
	/**
	 * Method to validate a regular expression string against a pattern.
	 */
	public boolean validatePatternProperty(String pattern, String property);
}
