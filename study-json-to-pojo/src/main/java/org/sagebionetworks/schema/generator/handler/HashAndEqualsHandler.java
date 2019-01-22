package org.sagebionetworks.schema.generator.handler;

import org.sagebionetworks.schema.ObjectSchema;

import com.sun.codemodel.JDefinedClass;

/**
 * This handler is responsible for adding both hashCode() and equals() to a POJO.
 * @author jmhill
 *
 */
public interface HashAndEqualsHandler {
	
	/**
	 * Add both hashCode() and equals() to the POJO
	 * @param classSchema
	 * @param classType
	 */
	public void addHashAndEquals(ObjectSchema classSchema, JDefinedClass classType);

}
