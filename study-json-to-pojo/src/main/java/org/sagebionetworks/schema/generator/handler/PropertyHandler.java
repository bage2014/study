package org.sagebionetworks.schema.generator.handler;

import org.sagebionetworks.schema.ObjectSchema;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JType;

public interface PropertyHandler {
	

	/**
	 * Create a property for the given name and type.
	 * @param propertySchema
	 * @param classType
	 * @param propertyName
	 * @param propertyType
	 * @return
	 */
	public JFieldVar createProperty(ObjectSchema propertySchema, JDefinedClass classType, String propertyName, JType propertyType);

}
