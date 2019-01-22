package org.sagebionetworks.schema.generator.handler.schema03;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.sagebionetworks.schema.EnumValue;
import org.sagebionetworks.schema.ExtraFields;
import org.sagebionetworks.schema.FORMAT;
import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.TYPE;
import org.sagebionetworks.schema.adapter.AdapterCollectionUtils;
import org.sagebionetworks.schema.adapter.JSONArrayAdapter;
import org.sagebionetworks.schema.adapter.JSONEntity;
import org.sagebionetworks.schema.adapter.JSONMapAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.generator.InstanceFactoryGenerator;
import org.sagebionetworks.schema.generator.handler.JSONMarshalingHandler;

import com.sun.codemodel.ClassType;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCatchBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JCommentPart;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JForEach;
import com.sun.codemodel.JForLoop;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JTryBlock;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;
import com.sun.codemodel.JWhileLoop;

public class JSONMarshalingHandlerImpl03 implements JSONMarshalingHandler{

	private static final String VAR_PREFIX = "__";

	@Override
	public void addJSONMarshaling(ObjectSchema classSchema,	JDefinedClass classType, InstanceFactoryGenerator interfaceFactoryGenerator) {
		// There is nothing to do for interfaces.
		if(TYPE.INTERFACE == classSchema.getType()){
			throw new IllegalArgumentException("Cannot add marshaling to an interface");
		}
		// Make sure this class implements JSONEntity
		// classType._implements(JSONEntity.class);

		// Create a field to handle overflow from newer type definitions
		//JFieldVar extraFields = createMissingFieldField(classType);

		// Create the init method
		//JMethod initMethod = createMethodInitializeFromJSONObject(classSchema, classType, interfaceFactoryGenerator);
		// setup a constructor.
		//createConstructor(classSchema, classType, initMethod);
		
		// Add the second method.
		//createWriteToJSONObject(classSchema, classType);
	}

	JFieldVar createMissingFieldField(JDefinedClass classType) {
		JFieldVar extraFields = classType.field(JMod.PRIVATE, classType.owner().ref(Map.class).narrow(String.class).narrow(Object.class),
				ObjectSchema.EXTRA_FIELDS, JExpr._null());
		return extraFields;
	}
	
	/**
	 * 
	 * @param classSchema
	 * @param classType
	 * @return
	 */
	protected JMethod createConstructor(ObjectSchema classSchema, JDefinedClass classType, JMethod initializeMethod) {
		// We always need a no-args constructor
		classType.constructor(JMod.PUBLIC);
		// Now the constructor that takes a JSONObjectAdapter.
		JMethod constructor  = classType.constructor(JMod.PUBLIC);
		constructor._throws(JSONObjectAdapterException.class);
		// add the parameter
		JVar param = constructor.param(classType.owner()._ref(JSONObjectAdapter.class), "adapter");
		JDocComment docs = constructor.javadoc();
		docs.add("Marshal a new "+classType.name()+" from JSON using the provided implementation of "+JSONObjectAdapter.class.getName());
		JCommentPart part = docs.addParam(param);
		part.add("Data will be read from this adapter to populate this object.");
		docs.addThrows(JSONObjectAdapterException.class);
		// Create the constructor body
        JBlock body = constructor.body();
		// First add a super call
        if(classSchema.getExtends() != null){
        	JInvocation invocation = JExpr.invoke("super").arg(param);
        	body.add(invocation);
        }else{
        	JInvocation invocation = JExpr.invoke("super");
        	body.add(invocation);
        }
        JFieldRef staticMessageRef = classType.owner().ref(ObjectSchema.class).staticRef("OBJECT_ADAPTER_CANNOT_BE_NULL");
        // Make sure the parameter is not null
        body._if(param.eq(JExpr._null()))
        	._then()._throw(createIllegalArgumentException(classType, staticMessageRef));
        
        // Now invoke the init method
        body.invoke(initializeMethod).arg(param);
		return constructor;
	}
	
	/**
	 * Create the InitializeFromJSONObject method for the JSONEntity interface.
	 * 
	 * @param classSchema
	 * @param classType
	 * @param createRegister 
	 * @return
	 */
	protected JMethod createMethodInitializeFromJSONObject(ObjectSchema classSchema, JDefinedClass classType, InstanceFactoryGenerator interfaceFactoryGenerator) {
		// Now the method that takes a JSONObjectAdapter.
		JMethod method = createBaseMethod(classSchema, classType, "initializeFromJSONObject");
		JVar param = method.params().get(0);
		JBlock body = method.body();
		
		// First validate against the schema
		JFieldVar allKeyNames = classType.fields().get(ObjectSchema.ALL_KEYS_NAME);
		
		JInvocation invocation = classType.owner().ref(ExtraFields.class).staticInvoke("createExtraFieldsMap")
				.arg(param);
		if(allKeyNames != null) {
			invocation.arg(allKeyNames);
		}
		JFieldVar extraFields = classType.fields().get(ObjectSchema.EXTRA_FIELDS);
		if (extraFields == null) {
			throw new IllegalArgumentException("Failed to find the JFieldVar for property: '" + ObjectSchema.EXTRA_FIELDS + "' on class: "
					+ classType.name());
		}
		body.assign(extraFields, invocation);
		
		JFieldRef conreteTypeRef = classType.owner().ref(ObjectSchema.class).staticRef("CONCRETE_TYPE");
        
		// Now process each property
		Map<String, ObjectSchema> fieldMap = classSchema.getObjectFieldMap();
		Iterator<String> keyIt = fieldMap.keySet().iterator();
		while (keyIt.hasNext()) {
			String propName = keyIt.next();
			ObjectSchema propSchema = fieldMap.get(propName);
			// Look up the field for this property
			JFieldVar field = getPropertyRefence(classType, propName);
			JFieldVar propNameConstant = getPropertyKeyConstantReference(classType, propName);

			// Now process this field
			if (propSchema.getType() == null)
				throw new IllegalArgumentException("Property: '" + propSchema
						+ "' has a null TYPE on class: " + classType.name());
			TYPE type = propSchema.getType();
			if (type.isPrimitive()) {
				body.assign(field,
						param.invoke(type.getMethodName()).arg(propNameConstant));
				continue;
			}
			// Add an if
			
			JConditional hasCondition = body._if(param.invoke("isNull").arg(
					propNameConstant).not());
			JBlock thenBlock = hasCondition._then();
			// For strings and primitives we can just assign the value right
			// from the adapter.
			if (TYPE.STRING == type) {
				// The format determines how JSON strings are read.
				JExpression rhs = null;
				if(propSchema.getEnum() != null){
					// Assign an enum
					rhs = assignJSONStringToEnumProperty(param, propNameConstant, field);
					thenBlock.assign(field, rhs);
				}else{
					// This is just a string.
					rhs = assignJSONStringToProperty(classType.owner(),
							param, propNameConstant, propSchema);
					thenBlock.assign(field, rhs);
				}

			}else if (TYPE.BOOLEAN == type || TYPE.NUMBER == type || TYPE.INTEGER == type) {
				JClass typeClass = (JClass) field.type();
				// Basic assign
				thenBlock.assign(field, JExpr._new(typeClass).arg(param.invoke(type.getMethodName()).arg(propNameConstant)));
			} else if (TYPE.ARRAY == type) {
				// Determine the type of the field
				JClass typeClass = (JClass) field.type();
				if (typeClass.getTypeParameters().size() != 1)
					throw new IllegalArgumentException(
							"Cannot determine the type of an array: "
									+ typeClass.fullName());
				JClass arrayTypeClass = typeClass.getTypeParameters().get(0);
				ObjectSchema arrayTypeSchema = propSchema.getItems();
				if (arrayTypeSchema == null)
					throw new IllegalArgumentException(
							"A property type is ARRAY but the getItems() returned null");
				TYPE arrayType = arrayTypeSchema.getType();
				if (arrayType == null)
					throw new IllegalArgumentException(
							"TYPE cannot be null for an ObjectSchema");
				// Type arrayType =
				if (!propSchema.getUniqueItems()) {
					// Create a list
					thenBlock.assign(
							field,
							JExpr._new(classType.owner().ref(ArrayList.class)
									.narrow(arrayTypeClass)));
				} else {
					// Create a set
					thenBlock.assign(
							field,
							JExpr._new(classType.owner().ref(HashSet.class)
									.narrow(arrayTypeClass)));
				}
				// Create a local array
				JVar jsonArray = thenBlock.decl(classType.owner().ref(JSONArrayAdapter.class), VAR_PREFIX + "jsonArray",
						param.invoke("getJSONArray").arg(propNameConstant));
				JForLoop loop = thenBlock._for();
				JVar i = loop.init(classType.owner().INT, VAR_PREFIX + "i", JExpr.lit(0));
				loop.test(i.lt(jsonArray.invoke("length")));
				loop.update(i.incr());
				JBlock loopBody = loop.body();
				// Handle abstract classes and interfaces
				if(arrayTypeClass.isInterface() || arrayTypeClass.isAbstract()){
					if(interfaceFactoryGenerator == null) throw new IllegalArgumentException("A InterfaceFactoryGenerator is need to create interfaces or abstract classes.");
					JDefinedClass createRegister = interfaceFactoryGenerator.getFactoryClass(arrayTypeClass);
					JConditional ifNull = loopBody._if(jsonArray.invoke("isNull").arg(i));
					// if null
					JBlock ifNulThenBlock = ifNull._then();
					// then add(null)
					ifNulThenBlock.add(field.invoke("add").arg(JExpr._null()));
					// else add(value)
					JBlock ifNullElseBlock = ifNull._else();
					// first get the JSONObject for this array element
					JVar indexAdapter = ifNullElseBlock.decl(classType.owner()._ref(JSONObjectAdapter.class), VAR_PREFIX + "indexAdapter", jsonArray
							.invoke("getJSONObject").arg(i));
					// Create the object from the register
					JVar indexObject = ifNullElseBlock.decl(
							arrayTypeClass,
							VAR_PREFIX + "indexObject",
							JExpr.cast(
									arrayTypeClass,
									createRegister.staticInvoke("singleton").invoke("newInstance")
											.arg(indexAdapter.invoke("getString").arg(conreteTypeRef))));
					// Initialize the object from the adapter.
					ifNullElseBlock.add(indexObject.invoke("initializeFromJSONObject").arg(indexAdapter));
					// add the object to the list
					ifNullElseBlock.add(field.invoke("add").arg(indexObject));
				}else{
					// concrete classes
					loopBody.add(field.invoke("add").arg(
							createIsNullCheck(jsonArray, i,
									createExpresssionToGetFromArray(param, jsonArray, arrayTypeSchema, arrayTypeClass, i))));
				}

			} else if (TYPE.MAP == type) {
				// Determine the type of the key
				JClass typeClass = (JClass) field.type();
				if (typeClass.getTypeParameters().size() != 2)
					throw new IllegalArgumentException(
							"Cannot determine the key and value type of a map: "
									+ typeClass.fullName());
				ObjectSchema keyTypeSchema = propSchema.getKey();
				if (keyTypeSchema == null)
					throw new IllegalArgumentException("A property type is MAP but the getKey() returned null");
				ObjectSchema valueTypeSchema = propSchema.getValue();
				if (valueTypeSchema == null)
					throw new IllegalArgumentException("A property type is MAP but the getValue() returned null");
				JClass keyTypeClass = typeClass.getTypeParameters().get(0);
				JClass valueTypeClass = typeClass.getTypeParameters().get(1);
				thenBlock.assign(
						field,
						JExpr._new(classType.owner().ref(HashMap.class).narrow(keyTypeClass,valueTypeClass)));
				JVar jsonMap = thenBlock.decl(classType.owner().ref(JSONMapAdapter.class), VAR_PREFIX + "jsonMap", param.invoke("getJSONMap")
						.arg(propNameConstant));

				JType keyObject = classType.owner().ref(Object.class);
				JForEach loop = thenBlock.forEach(keyObject, VAR_PREFIX + "keyObject", jsonMap.invoke("keys"));
				JBlock loopBody = loop.body();
				// Handle abstract classes and interfaces
				JVar value = loopBody.decl(valueTypeClass, VAR_PREFIX + "value");
				JConditional ifNull = loopBody._if(jsonMap.invoke("isNull").arg(loop.var()));
				// if null
				JBlock ifNulThenBlock = ifNull._then();
				// then value = null
				ifNulThenBlock.assign(value, JExpr._null());
				// else
				JBlock ifNullElseBlock = ifNull._else();
				if (valueTypeClass.isInterface() || valueTypeClass.isAbstract()) {
					if (interfaceFactoryGenerator == null)
						throw new IllegalArgumentException("A InterfaceFactoryGenerator is need to create interfaces or abstract classes.");
					JDefinedClass createRegister = interfaceFactoryGenerator.getFactoryClass(valueTypeClass);
					// first get the JSONObject for this array element
					JVar valueAdapter = ifNullElseBlock.decl(classType.owner()._ref(JSONObjectAdapter.class), VAR_PREFIX + "valueAdapter",
							jsonMap
							.invoke("getJSONObject").arg(loop.var()));

					// Create the object from the register
					ifNullElseBlock.assign(
							value,
							JExpr.cast(
									valueTypeClass,
									createRegister.staticInvoke("singleton").invoke("newInstance")
											.arg(valueAdapter.invoke("getString").arg(conreteTypeRef))));
					// Initialize the object from the adapter.
					ifNullElseBlock.add(value.invoke("initializeFromJSONObject").arg(valueAdapter));
				} else {
					ifNullElseBlock.assign(value, createExpressionToGetFromMap(param, jsonMap, loop.var(), valueTypeSchema, valueTypeClass));
				}
				JVar key = loopBody.decl(keyTypeClass, VAR_PREFIX + "key",
						createExpressionToGetKey(param, loop.var(), keyTypeSchema, keyTypeClass));
				loopBody.add(field.invoke("put").arg(key).arg(value));
			} else {
				// First extract the type
				// If we have a register then we need to use it
				JClass typeClass = (JClass) field.type();
				if(typeClass.isInterface() || typeClass.isAbstract()){
					if(interfaceFactoryGenerator == null) throw new IllegalArgumentException("A InterfaceFactoryGenerator is need to create interfaces or abstract classes.");
					JDefinedClass createRegister = interfaceFactoryGenerator.getFactoryClass(typeClass);
					// Use the register to create the class
					JVar localAdapter = thenBlock.decl(classType.owner().ref(JSONObjectAdapter.class), VAR_PREFIX + "localAdapter", param
							.invoke("getJSONObject").arg(propNameConstant));
					thenBlock.assign(field, JExpr.cast(field.type(), createRegister.staticInvoke("singleton").invoke("newInstance").arg(localAdapter.invoke("getString").arg(conreteTypeRef))));
					thenBlock.add(field.invoke("initializeFromJSONObject").arg(localAdapter));

				}else{
					// We can just create a new type for this object.
					thenBlock.assign(
							field,
							JExpr._new(typeClass).arg(
									param.invoke("getJSONObject").arg(propNameConstant)));
				}

			}
			// throw an exception it this is a required fields
			if (propSchema.isRequired() && propSchema.getDefault() == null) {				
				hasCondition._else()
						._throw(createIllegalArgumentExceptionPropertyNotNull(classType, propNameConstant));
			} else {
				//if propSchema has a default defined the property must
				//be assigned to that default when  the adapter doesn't
				//have a corresponding property
				if (propSchema.getDefault() == null){
					// For non-require properties set the property to null
					hasCondition._else().assign(field, JExpr._null());
				}
				else {
					JExpression propShouldBe = assignDefaultProperty(propSchema);
					hasCondition._else().assign(field, propShouldBe);
				}
				
			}
		}
        // Always return the param
        body._return(param);
		return method;
	}

	private JFieldVar getPropertyKeyConstantReference(JDefinedClass classType, String propName) {
		JFieldVar propNameConstant = classType.fields().get(ObjectSchema.getKeyConstantName(propName));
		if (propNameConstant == null) {
			throw new IllegalArgumentException(
					"Failed to find the JFieldVar for constant property name: '"
							+ propName + "' on class: " + classType.name());
		}
		return propNameConstant;
	}



	private JExpression createIsNullCheck(JVar jsonArray, JVar index, JExpression createExpresssionToGetFromArray) {
		return JOp.cond(jsonArray.invoke("isNull").arg(index), JExpr._null(), createExpresssionToGetFromArray);
	}

	/**
	 * Assign a JSON String to a property.  The format determines how it will be treated.
	 * @param param
	 * @param propName
	 * @param field
	 * @param propertySchema
	 * @param block
	 */
	protected JExpression assignJSONStringToProperty(JCodeModel model, JVar adapter, JVar propName, ObjectSchema propertySchema) {
		// The format determines how to treat a string.
		FORMAT format = propertySchema.getFormat();
		JExpression stringFromAdapter = adapter.invoke(TYPE.STRING.getMethodName()).arg(propName);
		return convertStringAsNeeded(model, adapter, format, stringFromAdapter);
	}

	/**
	 * If the string needst to be converted then do so.
	 * @param model
	 * @param adapter
	 * @param format
	 * @param stringFromAdapter
	 * @return
	 */
	public JExpression convertStringAsNeeded(JCodeModel model, JVar adapter, FORMAT format, JExpression stringFromAdapter) {
		if(format == null || format == FORMAT.URI){
			// Null format is treated as a simple string.
			return stringFromAdapter;
		}else{
			// Each format is handled separately.
			if(format == FORMAT.DATE_TIME || format == FORMAT.DATE || format == FORMAT.TIME){
				// These are all date formats
				// Use the adapter to adapter to convert from a string to a date
				return adapter.invoke("convertStringToDate").arg(model.ref(FORMAT.class).staticInvoke("valueOf").arg(format.name())).arg(stringFromAdapter);
			}else {
				throw new IllegalArgumentException("Unsupporetd format: "+format);
			}
		}
	}
	
	public JExpression convertLongAsNeeded(JCodeModel model, JVar adapter, FORMAT format, JExpression value) {
		if(format == null ){
			// Null format is treated as a simple long.
			return value;
		}else{
			// Each format is handled separately.
			if(format == FORMAT.UTC_MILLISEC){
				// Create a new date from the UTC string
				return JExpr._new(model.ref(Date.class)).arg(value);
			}else {
				throw new IllegalArgumentException("Unsupporetd format: "+format);
			}
		}
	}
	
	/**
	 * Assign a JSON String to a property.  The format determines how it will be treated.
	 * @param param
	 * @param propName
	 * @param field
	 * @param propertySchema
	 * @param block
	 */
	protected JExpression assignJSONStringToEnumProperty(JVar adapter, JVar propName, JFieldVar field) {
		// The format determines how to treat a string.
		JExpression stringFromAdapter = adapter.invoke(TYPE.STRING.getMethodName()).arg(propName);
		JClass enumClass = (JClass) field.type();
		return enumClass.staticInvoke("valueOf").arg(stringFromAdapter);
	}
	
	/**
	 * Assign a property to a  JSON String.  The format determines how it will be treated.
	 * @param param
	 * @param propName
	 * @param field
	 * @param propertySchema
	 * @param block
	 */
	protected JExpression assignPropertyToJSONString(JCodeModel model, JVar adapter, ObjectSchema propertySchema, JExpression field) {
		// The format determines how to treat a string.
		FORMAT format = propertySchema.getFormat();
//		JExpression stringFromAdapter = adapter.invoke(TYPE.STRING.getMethodName()).arg(propName);
		if(format == null || format == FORMAT.URI){
			// Null format is treated as a simple string.
			return field;
		}else{
			// Each format is handled separately.
			if(format == FORMAT.DATE_TIME || format == FORMAT.DATE || format == FORMAT.TIME){
				// These are all date formats
				// Use the adapter to adapter to convert from a string to a date
				return adapter.invoke("convertDateToString").arg(model.ref(FORMAT.class).staticInvoke("valueOf").arg(format.name())).arg(field);
			}else{
				throw new IllegalArgumentException("Unsupporetd format: "+format);
			}
		}
	}
	
	protected JExpression assignPropertyToJSONLong(JCodeModel model, ObjectSchema propertySchema, JExpression field) {
		// The format determines how to treat a string.
		FORMAT format = propertySchema.getFormat();
		if(format == null ){
			// Null format is treated as a simple string.
			return field;
		}else{
			// Each format is handled separately.
			if(format == FORMAT.UTC_MILLISEC){
				// Use the long to create a date
				return field.invoke("getTime");
			}else{
				throw new IllegalArgumentException("Unsupporetd format: "+format);
			}
		}
	}
	
	protected JExpression createExpresssionToGetFromArray(JVar adapter, JVar jsonArray, ObjectSchema arrayTypeSchema, JClass arrayTypeClass,
			JVar index) {
		TYPE arrayType = arrayTypeSchema.getType();
		FORMAT arrayFormat = arrayTypeSchema.getFormat();
		//check if our array type is an enum
		if (!arrayTypeClass.isPrimitive() && !arrayTypeClass.fullName().equals("java.lang.String") && arrayTypeClass instanceof JDefinedClass) {
			JDefinedClass getTheClass = (JDefinedClass) arrayTypeClass;
			ClassType shouldHaveEnum = getTheClass.getClassType();
			if (ClassType.ENUM == shouldHaveEnum){
				//here we know we are dealing with an enum
				JExpression stringFromAdapter = jsonArray.invoke(arrayType.getMethodName()).arg(index);
				return arrayTypeClass.staticInvoke("valueOf").arg(stringFromAdapter);
			}
		}
		
		if(arrayType.isPrimitive() || TYPE.NUMBER == arrayType || TYPE.BOOLEAN == arrayType){
			return jsonArray.invoke(arrayType.getMethodName()).arg(index);
		}else if(TYPE.INTEGER == arrayType){
			JExpression longExper = jsonArray.invoke(arrayType.getMethodName()).arg(index);
			return convertLongAsNeeded(arrayTypeClass.owner(), adapter, arrayFormat, longExper);
		}else if(TYPE.STRING == arrayType){
			JExpression stringExper = jsonArray.invoke(arrayType.getMethodName()).arg(index);
			return convertStringAsNeeded(arrayTypeClass.owner(), adapter, arrayFormat, stringExper);
		}else if(TYPE.ARRAY == arrayType){
			throw new IllegalArgumentException("Arrays of Arrays are currently not supported");
		} else if (TYPE.MAP == arrayType) {
			throw new IllegalArgumentException("Arrays of Maps are currently not supported");
		}else{
			// Now we need to create an object of the the type
			return JExpr._new(arrayTypeClass).arg(jsonArray.invoke("getJSONObject").arg(index));
		}
	}
	
	protected JExpression createExpressionToGetFromMap(JVar adapter, JExpression jsonMap, JVar jsonKey, ObjectSchema typeSchema,
			JClass typeClass) {
		TYPE type = typeSchema.getType();
		FORMAT format = typeSchema.getFormat();
		//check if our array type is an enum
		String methodName = type.getMethodName();
		if (!typeClass.isPrimitive() && !typeClass.fullName().equals("java.lang.String") && typeClass instanceof JDefinedClass) {
			JDefinedClass getTheClass = (JDefinedClass) typeClass;
			ClassType shouldHaveEnum = getTheClass.getClassType();
			if (ClassType.ENUM == shouldHaveEnum){
				//here we know we are dealing with an enum
				JExpression stringFromAdapter = jsonMap.invoke(methodName).arg(jsonKey);
				return typeClass.staticInvoke("valueOf").arg(stringFromAdapter);
			}
		}
		
		if(type.isPrimitive() || TYPE.NUMBER == type || TYPE.BOOLEAN == type){
			return jsonMap.invoke(methodName).arg(jsonKey);
		}else if(TYPE.INTEGER == type){
			JExpression longExper = jsonMap.invoke(methodName).arg(jsonKey);
			return convertLongAsNeeded(typeClass.owner(), adapter, format, longExper);
		}else if(TYPE.STRING == type){
			JExpression stringExper = jsonMap.invoke(methodName).arg(jsonKey);
			return convertStringAsNeeded(typeClass.owner(), adapter, format, stringExper);
		}else if(TYPE.ARRAY == type){
			throw new IllegalArgumentException("Arrays of Arrays are currently not supported");
		} else if (TYPE.MAP == type) {
			throw new IllegalArgumentException("Arrays of Maps are currently not supported");
		}else{
			// Now we need to create an object of the the type
			return JExpr._new(typeClass).arg(jsonMap.invoke("getJSONObject").arg(jsonKey));
		}
	}

	protected JExpression createExpressionToGetKey(JVar adapter, JVar jsonValue, ObjectSchema typeSchema,
			JClass typeClass) {
		TYPE type = typeSchema.getType();
		FORMAT format = typeSchema.getFormat();
		//check if our array type is an enum
		String methodName = type.getMethodName();
		if (!typeClass.isPrimitive() && !typeClass.fullName().equals("java.lang.String") && typeClass instanceof JDefinedClass) {
			JDefinedClass getTheClass = (JDefinedClass) typeClass;
			ClassType shouldHaveEnum = getTheClass.getClassType();
			if (ClassType.ENUM == shouldHaveEnum){
				//here we know we are dealing with an enum
				return typeClass.staticInvoke("valueOf").arg(JExpr.cast(typeClass.owner()._ref(String.class), jsonValue));
			}
		}
		
		if(type.isPrimitive() || TYPE.NUMBER == type || TYPE.BOOLEAN == type){
			return JExpr.cast(typeClass.owner()._ref(type.getClass()), jsonValue);
		}else if(TYPE.INTEGER == type){
			return convertLongAsNeeded(typeClass.owner(), adapter, format, JExpr.cast(typeClass.owner()._ref(String.class), jsonValue));
		}else if(TYPE.STRING == type){
			return convertStringAsNeeded(typeClass.owner(), adapter, format, JExpr.cast(typeClass.owner()._ref(String.class), jsonValue));
		}else if(TYPE.ARRAY == type){
			throw new IllegalArgumentException("Arrays of Arrays are currently not supported");
		} else if (TYPE.MAP == type) {
			throw new IllegalArgumentException("Arrays of Maps are currently not supported");
		}else{
			// Now we need to create an object of the the type
			return jsonValue;
		}
	}

	/**
	 * Create the base method that is common to both initializeFromJSONObject() and writeToJSONObject()
	 * @param classSchema
	 * @param classType
	 * @param methodName
	 * @return
	 */
	protected JMethod createBaseMethod(ObjectSchema classSchema, JDefinedClass classType, String methodName){
		JMethod method  = classType.method(JMod.PUBLIC, JSONObjectAdapter.class, methodName);
		method._throws(JSONObjectAdapterException.class);
		method.annotate(Override.class);
		// add the parameter
		JVar param = method.param(classType.owner()._ref(JSONObjectAdapter.class), "adapter");
		JDocComment docs = method.javadoc();
		docs.add("@see JSONEntity#initializeFromJSONObject(JSONObjectAdapter)");
		docs.add("\n");
		docs.add("@see JSONEntity#writeToJSONObject(JSONObjectAdapter)");
		JCommentPart part = docs.addParam(param);
		docs.addThrows(JSONObjectAdapterException.class);
		// Create the constructor body
        JBlock body = method.body();
		// First add a super call
        if(classSchema.getExtends() != null){
        	JInvocation invocation = JExpr._super().invoke(methodName).arg(param);
        	body.add(invocation);
        }
        JFieldRef staticMessageRef = classType.owner().ref(ObjectSchema.class).staticRef("OBJECT_ADAPTER_CANNOT_BE_NULL");
        // Make sure the parameter is not null
        body._if(param.eq(JExpr._null()))
        	._then()._throw(createIllegalArgumentException(classType, staticMessageRef));
        
        return method;
	}
	
	/**
	 * Create the write method, that pushes data to the JSONObject.
	 * @param classSchema
	 * @param classType
	 * @return
	 */
	protected JMethod createWriteToJSONObject(ObjectSchema classSchema, JDefinedClass classType) {
		JMethod method = createBaseMethod(classSchema, classType, "writeToJSONObject");
		JVar param = method.params().get(0);
		JBlock body = method.body();
		// Add the object type.
		this.getClass().getName();
		
		createWriteExtraFields(classType, body, param);

		// Now process each property
		Map<String, ObjectSchema> fieldMap = classSchema.getObjectFieldMap();
		Iterator<String> keyIt = fieldMap.keySet().iterator();
		while (keyIt.hasNext()) {
			String propName = keyIt.next();
			ObjectSchema propSchema = fieldMap.get(propName);
			// Look up the field for this property
			JFieldVar field = getPropertyRefence(classType, propName);
			JFieldVar propNameConstant = getPropertyKeyConstantReference(classType, propName);
			
			// Now process this field
			if (propSchema.getType() == null)
				throw new IllegalArgumentException("Property: '" + propSchema
						+ "' has a null TYPE on class: " + classType.name());
			TYPE type = propSchema.getType();
			FORMAT format = propSchema.getFormat();

			// Primitives are easy, just assign them
			if (field.type().isPrimitive() && format == null) {
				body.add(param.invoke("put").arg(propNameConstant).arg(field));
				continue;
			}
			// Add an if
			JConditional hasCondition = body._if(field.ne(JExpr._null()));
			JBlock thenBlock = hasCondition._then();
			// For strings and primitives we can just assign the value right
			// from the adapter.
			if (TYPE.STRING == type) {
				// call the set method using the field
				JExpression valueToPut = null;
				if(propSchema.getEnum() != null){
					// Write the enum as a JSON string
					valueToPut = field.invoke("name");;
				}else{
					// This is just a string
					valueToPut = assignPropertyToJSONString(
							classType.owner(), param, propSchema, field);
				}
				thenBlock.add(param.invoke("put").arg(propNameConstant)
						.arg(valueToPut));
			}else if (TYPE.INTEGER == type) {
				// Integers can be dates or longs
				JExpression expr = assignPropertyToJSONLong(classType.owner(), propSchema, field);
				// Basic assign
				thenBlock.add(param.invoke("put").arg(propNameConstant).arg(expr));
			} else if (TYPE.BOOLEAN == type || TYPE.NUMBER == type) {
				JClass typeClass = (JClass) field.type();
				// Basic assign
				thenBlock.add(param.invoke("put").arg(propNameConstant).arg(field));
			} else if (TYPE.ARRAY == type) {
				// Determine the type of the field
				JClass typeClass = (JClass) field.type();
				if (typeClass.getTypeParameters().size() != 1)
					throw new IllegalArgumentException(
							"Cannot determine the type of an array: "
									+ typeClass.fullName());
				JClass arrayTypeClass = typeClass.getTypeParameters().get(0);
				ObjectSchema arrayTypeSchema = propSchema.getItems();
				if (arrayTypeSchema == null)
					throw new IllegalArgumentException(
							"A property type is ARRAY but the getItems() returned null");
				TYPE arrayType = arrayTypeSchema.getType();
				if (arrayType == null)
					throw new IllegalArgumentException(
							"TYPE cannot be null for an ObjectSchema");
				// Create the new JSONArray
				JVar array = thenBlock.decl(JMod.NONE, classType.owner().ref(JSONArrayAdapter.class), VAR_PREFIX + "array",
						param.invoke("createNewArray"));
				JVar it = thenBlock.decl(JMod.NONE, classType.owner().ref(Iterator.class).narrow(arrayTypeClass), VAR_PREFIX + "it",
						field.invoke("iterator"));
				JVar index = thenBlock.decl(JMod.NONE, classType.owner().INT, VAR_PREFIX + "index", JExpr.lit(0));
				// Create a local array
				JWhileLoop loop = thenBlock._while(it.invoke("hasNext"));
				JBlock loopBody = loop.body();
				JVar value = loopBody.decl(arrayTypeClass, VAR_PREFIX + "value", it.invoke("next"));
				loopBody.add(array.invoke("put").arg(index)
						.arg(createEqNullCheck(value, createExpresssionToSetFromArray(arrayTypeSchema, arrayTypeClass, value, param))));
				loopBody.directStatement(VAR_PREFIX + "index++;");
				// Now set the new array
				thenBlock.add(param.invoke("put").arg(propNameConstant).arg(array));
			} else if (TYPE.MAP == type) {
				// Determine the type of the key
				JClass typeClass = (JClass) field.type();
				if (typeClass.getTypeParameters().size() != 2)
					throw new IllegalArgumentException("Cannot determine the key and value type of a map: " + typeClass.fullName());
				ObjectSchema keyTypeSchema = propSchema.getKey();
				if (keyTypeSchema == null)
					throw new IllegalArgumentException("A property type is MAP but the getKey() returned null");
				ObjectSchema valueTypeSchema = propSchema.getValue();
				if (valueTypeSchema == null)
					throw new IllegalArgumentException("A property type is MAP but the getValue() returned null");
				JClass keyTypeClass = typeClass.getTypeParameters().get(0);
				JClass valueTypeClass = typeClass.getTypeParameters().get(1);

				// Create the new JSONArray
				JVar map = thenBlock.decl(JMod.NONE, classType.owner().ref(JSONMapAdapter.class), VAR_PREFIX + "map",
						param.invoke("createNewMap"));
				JType entry = classType.owner().ref(Map.Entry.class).narrow(keyTypeClass, valueTypeClass);
				JForEach loop = thenBlock.forEach(entry, VAR_PREFIX + "entry", field.invoke("entrySet"));
				JBlock loopBody = loop.body();
				JConditional ifNull = loopBody._if(loop.var().invoke("getValue").eq(JExpr._null()));
				ifNull._then().add(map.invoke("putNull").arg(loop.var().invoke("getKey")));
				ifNull._else().add(
						map.invoke("put").arg(loop.var().invoke("getKey"))
								.arg(createExpresssionToSetFromMap(valueTypeSchema, valueTypeClass, loop.var().invoke("getValue"), param)));
				// Now set the new array
				thenBlock.add(param.invoke("put").arg(field.name()).arg(map));
			} else {
				// All others are treated as objects.
				thenBlock.add(param
						.invoke("put")
						.arg(propNameConstant)
						.arg(field.invoke("writeToJSONObject").arg(
								param.invoke("createNew"))));
			}
			// throw an exception it this is a required fields
			if (propSchema.isRequired()) {
				hasCondition._else()
				._throw(createIllegalArgumentExceptionPropertyNotNull(classType, propNameConstant));
			}
		}
        // Always return the param
        body._return(param);
        return method;
		
	}

	private JFieldVar getPropertyRefence(JDefinedClass classType, String propName) {
		JFieldVar field = classType.fields().get(propName);
		if (field == null)
			throw new IllegalArgumentException(
					"Failed to find the JFieldVar for property: '"
							+ propName + "' on class: " + classType.name());
		return field;
	}
	
	void createWriteExtraFields(JDefinedClass classType, JBlock body, JVar jsonObject) {
		JFieldVar extraFields = classType.fields().get(ObjectSchema.EXTRA_FIELDS);
		JBlock thenBlock = body._if(extraFields.ne(JExpr._null()))._then();
		thenBlock.staticInvoke(classType.owner().ref(AdapterCollectionUtils.class), "writeToObject").arg(jsonObject).arg(extraFields);
	}

	private JExpression createEqNullCheck(JVar value, JExpression createExpresssionToSetFromArray) {
		return JOp.cond(value.eq(JExpr._null()), JExpr._null(), createExpresssionToSetFromArray);
	}

	protected JExpression createExpresssionToSetFromArray(ObjectSchema arrayTypeSchema, JClass arrayTypeClass, JVar value, JVar param) {
		TYPE arrayType = arrayTypeSchema.getType();
		//need to determine if we are dealing with an array of enumerations
		if (!arrayTypeClass.isPrimitive() && !arrayTypeClass.fullName().equals("java.lang.String") && arrayTypeClass instanceof JDefinedClass){
			JDefinedClass getTheClass = (JDefinedClass)arrayTypeClass;
			ClassType shouldHaveEnum = getTheClass.getClassType();
			if (ClassType.ENUM == shouldHaveEnum){
				return value.invoke("name");
			}
		}
		
		if(arrayType.isPrimitive() || TYPE.NUMBER == arrayType || TYPE.BOOLEAN == arrayType){
			return value;
		}if(TYPE.STRING == arrayType){
			JExpression stringValue = value;
			return assignPropertyToJSONString(arrayTypeClass.owner(), param, arrayTypeSchema, stringValue); 
		}else if(TYPE.INTEGER == arrayType){
			return assignPropertyToJSONLong(arrayTypeClass.owner(), arrayTypeSchema, value);
		}else if(TYPE.ARRAY == arrayType){
			throw new IllegalArgumentException("Arrays of Arrays are currently not supported");
		} else if (TYPE.MAP == arrayType) {
			throw new IllegalArgumentException("Arrays of Maps are currently not supported");
		}else{
			// Now we need to create an object of the the type
			return value.invoke("writeToJSONObject").arg(param.invoke("createNew"));
		}
	}

	protected JExpression createExpresssionToSetFromMap(ObjectSchema typeSchema, JClass typeClass, JInvocation value, JVar param) {
		TYPE type = typeSchema.getType();
		// need to determine if we are dealing with an array of enumerations
		if (!typeClass.isPrimitive() && !typeClass.fullName().equals("java.lang.String") && typeClass instanceof JDefinedClass) {
			JDefinedClass getTheClass = (JDefinedClass) typeClass;
			ClassType shouldHaveEnum = getTheClass.getClassType();
			if (ClassType.ENUM == shouldHaveEnum) {
				return value.invoke("name");
			}
		}

		if (type.isPrimitive() || TYPE.NUMBER == type || TYPE.BOOLEAN == type) {
			return value;
		}
		if (TYPE.STRING == type) {
			return assignPropertyToJSONString(typeClass.owner(), param, typeSchema, value);
		} else if (TYPE.INTEGER == type) {
			return assignPropertyToJSONLong(typeClass.owner(), typeSchema, value);
		} else if (TYPE.ARRAY == type) {
			throw new IllegalArgumentException("Maps of Arrays are currently not supported");
		} else if (TYPE.MAP == type) {
			throw new IllegalArgumentException("Maps of Maps are currently not supported");
		} else {
			// Now we need to create an object of the the type
			return value.invoke("writeToJSONObject").arg(param.invoke("createNew"));
		}
	}

	/**
	 * Helper to create a new IllegalArgumentException 
	 * @param classType
	 * @param message
	 * @return
	 */
	private JInvocation createIllegalArgumentException(JDefinedClass classType, JExpression message){
		return JExpr._new(classType.owner().ref(IllegalArgumentException.class)).arg(message);
	}
	
	/**
	 * Create an IllegalArgumentException for "property cannot be null"
	 * @param classType
	 * @param propConstRef Reference to the property constant name.
	 * @return
	 */
	private JInvocation createIllegalArgumentExceptionPropertyNotNull(JDefinedClass classType, JFieldVar propConstRef){
		JInvocation staticCall = classType.owner().ref(ObjectSchema.class).staticInvoke("createPropertyCannotBeNullMessage").arg(propConstRef);
		return JExpr._new(classType.owner().ref(IllegalArgumentException.class)).arg(staticCall);
	}

	/**
	 * Helper to assign a property a default value in the initializeFromJSONObject
	 * method that will be generated.  Handles situation where a property has a default
	 * value.
	 */
	private JExpression assignDefaultProperty(ObjectSchema propSchema){
		//determine what type the propSchema is
		TYPE type = propSchema.getType();
		JExpression propShouldBe = null;
		if (type == null){
			throw new IllegalArgumentException("property " + propSchema + 
					" has an null type and so a default can not " +
					"be assigned for this property");
		}
		if (TYPE.STRING == type){
			String defaultAsString = (String)propSchema.getDefault();
			propShouldBe = JExpr.lit(defaultAsString);
		}
		else if (TYPE.NUMBER == type){
			double defaultLong = (Double)propSchema.getDefault();
			propShouldBe = JExpr.lit(defaultLong);
		}
		else if (TYPE.INTEGER == type){
			long defaultLong = (Long)propSchema.getDefault();
			propShouldBe = JExpr.lit(defaultLong);
		}
		else if (TYPE.BOOLEAN == type){
			boolean defaultBoolean = (Boolean)propSchema.getDefault();
			propShouldBe = JExpr.lit(defaultBoolean);
		}
		else {
			throw new RuntimeException("can't assign default value " 
					+ propSchema.getDefault() + " as it is not a supported type");
		}
		return propShouldBe;
	}

	public JMethod createMethodInitializeFromJSONObject(ObjectSchema schema, JDefinedClass sampleClass) {
		return createMethodInitializeFromJSONObject(schema, sampleClass, null);
	}
}
