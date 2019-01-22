package org.sagebionetworks.schema.generator.handler;

import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.generator.InstanceFactoryGenerator;

import com.sun.codemodel.JDefinedClass;

public interface JSONMarshalingHandler {

	/**
	 * Add the JSON marshaling to an object.
	 * @param propertySchema
	 * @param classType
	 */
	public void addJSONMarshaling(ObjectSchema classSchema, JDefinedClass classType, InstanceFactoryGenerator interfaceFactoryGenerator);
}
