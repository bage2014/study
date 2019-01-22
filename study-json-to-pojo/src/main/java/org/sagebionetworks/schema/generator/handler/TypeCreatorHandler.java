package org.sagebionetworks.schema.generator.handler;

import org.sagebionetworks.schema.ObjectSchema;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JType;

/**
 * This handler 
 * @author jmhill
 *
 */
public interface TypeCreatorHandler {
	
	/**
	 * Create a new type for a given object schema.
	 * 
	 * @param _package
	 * @param schema - The schema to create a type for.
	 * @param superType - If the schema extends another type, this will type will be be provided.
	 * @param arrayType - If the type is an array, the array type will be provided.
	 * @param keyType - If the type is a map, the key type will be provided.
	 * @param valueType - If the type is an map, the value type will be provided.
	 * @return - The type created.
	 * @throws ClassNotFoundException
	 */
	public JType handelCreateType(JCodeModel model, ObjectSchema schema, JType superType, JType arrayType, JType keyType, JType valueType,
			JType[] interfanceTypes) throws ClassNotFoundException;

}
