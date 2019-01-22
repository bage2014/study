package org.sagebionetworks.schema.generator.handler;

/**
 * Provides the various handlers to the PojoGeneratrorDriver.  Each implementation can be schema specific.
 * @author jmhill
 *
 */
public interface HandlerFactory {
	
	/**
	 * This handler must create the types for every schema founds.
	 * @return
	 */
	public  TypeCreatorHandler getTypeCreatorHandler();
	
	/**
	 * This handler must add all of the properties to the POJO
	 * @return
	 */
	public PropertyHandler getPropertyHandler();
	
	/**
	 * This handler must add the JSON marshaling to the POJO
	 * @return
	 */
	public JSONMarshalingHandler getJSONMArshalingHandler();

	/**
	 * This handler must add hashCode() and equals()
	 * @return
	 */
	public HashAndEqualsHandler getHashAndEqualsHandler();

	/**
	 * This handler must add toString()
	 * @return
	 */
	public ToStringHandler getToStringHandler();

}
