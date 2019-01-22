package org.sagebionetworks.schema.generator.handler;

import org.sagebionetworks.schema.ObjectSchema;

import com.sun.codemodel.JDefinedClass;

/**
 * This handler is responsible for adding toString() to a POJO.
 * @author ntiedema
 *
 */
public interface ToStringHandler {
	/**
	 * Add toString() to the POJO
	 * @param classSchema
	 * @param classType
	 */
	public void addToStringMethod(ObjectSchema classSchema, JDefinedClass classType);
}
