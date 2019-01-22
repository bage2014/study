package org.sagebionetworks.schema.generator.handler.schema03;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sagebionetworks.schema.EnumValue;
import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.TYPE;
import org.sagebionetworks.schema.adapter.JSONEntity;
import org.sagebionetworks.schema.generator.handler.TypeCreatorHandler;

import com.sun.codemodel.JArray;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JEnumConstant;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;

/**
 * Handles type creation for version 03 
 * @see <a
 *      href="http://tools.ietf.org/html/draft-zyp-json-schema-03">http://tools.ietf.org/html/draft-zyp-json-schema-03</a>
 * @author jmhill
 *
 */
public class TypeCreatorHandlerImpl03 implements TypeCreatorHandler {
	
	
	public static final String AUTO_GENERATED_MESSAGE = "Note: This class was auto-generated, and should not be directly modified.";


	@Override
	public JType handelCreateType(JCodeModel codeModel, ObjectSchema schema, JType superType, JType arrayType, JType keyType,
			JType valueType, JType[] interfanceTypes) throws ClassNotFoundException {
		// Is this an enumeration
		if(schema.getEnum() != null){
			// Create or get the enumeration class.
			return createOrGetEnum(codeModel, schema);
		}
		if(superType.isPrimitive()) return superType;
		
		// Determine the type of this class
		if(schema.getType() == null) throw new IllegalArgumentException("TYPE cannot be null");
		
		// First check to see if this is a date format
		if(schema.getFormat() != null){
			if(schema.getFormat().isDateFormat()){
				// If the format is a date then the java type is a date.
				return codeModel.ref(Date.class);
			}
		}
		
		// Handle primitives
		if(schema.getType().isPrimitive()){
			// This is a primitive
			return codeModel.parseType(schema.getType().getJavaType());
		}

		// Strings
		if(TYPE.STRING == schema.getType()){
			return codeModel.ref(String.class);
		}
		if(TYPE.NUMBER == schema.getType()){
			return codeModel.ref(Double.class);
		}
		if(TYPE.BOOLEAN == schema.getType()){
			return codeModel.ref(Boolean.class);
		}
		if(TYPE.INTEGER == schema.getType()){
			return codeModel.ref(Long.class);
		}
		// Any is treated as a generic object
		if(TYPE.ANY == schema.getType()){
			return codeModel.ref(Object.class);
		}
		// Null is treated as object
		if(TYPE.NULL == schema.getType()){
			return codeModel.ref(Object.class);
		}
		if(TYPE.ARRAY == schema.getType()){
			// We must have Items
			if(arrayType == null) throw new IllegalArgumentException("A schema with TYPE.ARRAY must have a items that defines the type of the array");
			// Get the array type
			if(schema.getUniqueItems()){
				// This is a set
				return codeModel.ref(Set.class).narrow(arrayType);
			}else{
				// This is a list
				return codeModel.ref(List.class).narrow(arrayType);
			}
		}
		if(TYPE.MAP == schema.getType()){
			// We must have Items
			if (keyType == null)
				throw new IllegalArgumentException("A schema with TYPE.MAP must have a type for the key");
			// We must have Items
			if (valueType == null)
				throw new IllegalArgumentException("A schema with TYPE.MAP must have a type for the value");
			// This is a map
			return codeModel.ref(Map.class).narrow(keyType.boxify(), valueType.boxify());
		}
		// The last category is objects.
		if(TYPE.OBJECT == schema.getType() || TYPE.INTERFACE == schema.getType()){
			return createOrGetClassAndInterface(codeModel, schema, superType,
					interfanceTypes);
		}
		// Any other type is a failure
		throw new IllegalArgumentException("Unknown type: "+schema.getType());
	}


	/**
	 * Create or get a class or interface for the given schema.
	 * @param _package
	 * @param schema
	 * @param superType
	 * @param interfanceTypes
	 * @return
	 */
	private JType createOrGetClassAndInterface(JCodeModel codeModel, ObjectSchema schema, JType superType, JType[] interfanceTypes) {
		// A named object is a new class while a null name is a generic object
		if(schema.getName() == null){
			return codeModel.ref(Object.class);
		}
		JPackage _package = codeModel._package(schema.getPackageName());
		// Create a new class with this name
		try {
			JDefinedClass newClass = null;
			if(TYPE.OBJECT == schema.getType()){
				newClass = _package._class(schema.getName());
				newClass._implements(Serializable.class);
			}else if(TYPE.INTERFACE == schema.getType()){
				newClass = _package._interface(schema.getName());
			}else{
				throw new IllegalArgumentException("Unknown type: "+schema.getType()); 
			}
			// Both classes and interfaces should implement JSONEntity:
			// newClass._implements(JSONEntity.class);
			if(superType != null && TYPE.OBJECT == schema.getType()){
				newClass._extends((JClass) superType);
			}
			if(interfanceTypes != null){
				for(JType interfacesType: interfanceTypes){
					newClass._implements((JClass)interfacesType);
				}
			}
			// add all of the key constants
			addKeyConstants(schema, newClass);
			// Add all of the comments
			addComments(schema, newClass);
			return newClass;
		} catch (JClassAlreadyExistsException e) {
			return e.getExistingClass();
		}
	}

	/**
	 * Create a key constant for each property of the schema.
	 * @param schema
	 * @param newClass
	 */
	public static void addKeyConstants(ObjectSchema schema, JDefinedClass newClass) {
		if(!newClass.isInterface()) {
			JType stringType = newClass.owner()._ref(String.class);
			JType stringArrayType = newClass.owner()._ref(String[].class);
			Map<String, ObjectSchema> fieldMap = schema.getObjectFieldMap();
			Iterator<String> keyIt = fieldMap.keySet().iterator();
			List<JExpression> keyConstants = new LinkedList<JExpression>();
			while (keyIt.hasNext()) {
				int mods = JMod.PRIVATE | JMod.STATIC | JMod.FINAL;
				String key = keyIt.next();
				String name = ObjectSchema.getKeyConstantName(key);
				JExpression value = JExpr.lit(key);
				JFieldVar keyConst= newClass.field(mods, stringType , name, value);
				keyConstants.add(keyConst);
			}
			if(!keyConstants.isEmpty()) {
				// create an array for all all values
				int mods = JMod.PRIVATE | JMod.STATIC | JMod.FINAL;
				JType type = newClass.owner()._ref(String.class);
				JArray value = JExpr.newArray(type);
				for(JExpression constants: keyConstants) {
					value.add(constants);
				}
				// the array of all names.
				newClass.field(mods, stringArrayType , ObjectSchema.ALL_KEYS_NAME, value);
			}
		}
	}

	public void addComments(ObjectSchema schema, JDefinedClass newClass) {
		// Add the comments to the class
		JDocComment docs = newClass.javadoc();
		if(schema.getTitle() != null){
			docs.add(schema.getTitle());
			docs.add("\n\n");
		}
		if(schema.getDescription() != null){
			docs.add(schema.getDescription());
			docs.add("\n\n");
		}
		// Add the auto-generated message
		docs.add(AUTO_GENERATED_MESSAGE);
	}


	/**
	 * Create or get an Enum class.
	 * @param _package
	 * @param schema
	 * @return
	 */
	private JType createOrGetEnum(JCodeModel codeModel, ObjectSchema schema) {
		if(TYPE.STRING != schema.getType()) throw new IllegalArgumentException("Enumerations cannot be of type: "+schema.getType()+".  Enumerations can only be of type: "+TYPE.STRING);
		if(schema.getName() == null) throw new IllegalArgumentException("Enumerations must have a name");
		try {
			JPackage _package = codeModel._package(schema.getPackageName());
			JDefinedClass enumClass = _package._enum(schema.getName());
			// Generate the enum constants
			for(EnumValue enumName: schema.getEnum()){
				JEnumConstant enumConst = enumClass.enumConstant(enumName.getName());
				if(enumName.getDescription() != null) {
					JDocComment doc = enumConst.javadoc();
					doc.add(enumName.getDescription());
				}
				
			}
			// Add all of the comments
			addComments(schema, enumClass);
			return enumClass;
		} catch (JClassAlreadyExistsException e) {
			return e.getExistingClass();
		}
	}

}
