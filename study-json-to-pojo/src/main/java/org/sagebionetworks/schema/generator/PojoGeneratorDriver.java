package org.sagebionetworks.schema.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.TYPE;
import org.sagebionetworks.schema.generator.handler.HandlerFactory;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;

/**
 * Drives the generation of POJOs from schema definitions. Since a JSON schema can reference many other JSON schemas
 * the POJO creation process becomes recursive.  The purpose of this driver is to handle the necessary recursion 
 * and push the rest of the work to non-recursive handlers.  Put another way, the driver drives the handlers.
 * 
 * @author jmhill
 *
 */
public class PojoGeneratorDriver {
	
	/*
	 * The factory serves up handlers used to do all of the non-recursive work.
	 */
	HandlerFactory factory = null;
	
	public PojoGeneratorDriver(HandlerFactory factory){
		if(factory == null) throw new IllegalArgumentException("The handler factory cannot be null");
		this.factory = factory;
	}
	
	
	/**
	 * Create all POJOs from the list of root schemas
	 * @param codeModel
	 * @param list
	 * @param registerClass
	 * @throws ClassNotFoundException
	 */
	public void createAllClasses(JCodeModel codeModel,	List<ObjectSchema> list) throws ClassNotFoundException {
		// The first step is to register all named types and replace all references with
		// concrete schemas.
		list = preprocessSchemas(list);
		// Provides all of the interface factories.
		InstanceFactoryGenerator interfaceFactoryGenerator = new InstanceFactoryGenerator(codeModel, list);
		// We are now ready to start creating the classes
		// First create the package
		JPackage _package = codeModel._package("");
		// Now recursively process all of the schema objects
		for(ObjectSchema schema: list){
			// Create each POJO
			createPOJO(codeModel, schema, interfaceFactoryGenerator);
		}
		// Last step is to build the factories.
		interfaceFactoryGenerator.buildFactories();
	}
	
	/**
	 * Create a complete class for a given schema.
	 * 
	 * @param _package
	 * @param schema
	 * @return
	 * @throws ClassNotFoundException
	 */
	public JDefinedClass createPOJO(JCodeModel codeModel, ObjectSchema schema, InstanceFactoryGenerator ifg) throws ClassNotFoundException{
		// First create the type for this schema
		JType type = createOrGetType(codeModel, schema);
		if(!(type instanceof JDefinedClass)) return null;
		JDefinedClass classType = (JDefinedClass) type;
		
		// If this is an enumeration then there is nothing left to add.
		if(schema.getEnum() != null){
			return classType;
		}
		// Process the properties
		Map<String, ObjectSchema> fieldMap = schema.getObjectFieldMap();
		Iterator<String> it = fieldMap.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			ObjectSchema propertySchema = fieldMap.get(key);
			// For nested sub-classes we need to make sure they have an id.
			if(propertySchema.getId() == null){
				if(propertySchema.getName() != null){
					// Inherit the outer class package.
					propertySchema.setId(schema.getPackageName()+"."+propertySchema.getName());
				}
			}
			// Get type type for this property
			JType propertyType = createOrGetType(codeModel, propertySchema);
			// Create this property
			factory.getPropertyHandler().createProperty(propertySchema,
					classType, key, propertyType);
		}
		if(TYPE.INTERFACE != schema.getType()){
			// Add the JSON marshaling
			factory.getJSONMArshalingHandler().addJSONMarshaling(schema, classType, ifg);
			// Add hash and equals
			factory.getHashAndEqualsHandler().addHashAndEquals(schema, classType);
			//add the toString
			factory.getToStringHandler().addToStringMethod(schema, classType);
		}

		return classType;
	}

	/**
	 * Pre-process all schema objects.  This will replace all references with concrete 
	 * schema objects.
	 * @param list
	 * @return
	 */
	List<ObjectSchema> preprocessSchemas(List<ObjectSchema> list) {
		Map<String, ObjectSchema> register = registerAllIdentifiedObjectSchemas(list);
		// Use the register to replace all references with their concrete objects
		list = findAndReplaceAllReferencesSchemas(register, list);
		return list;
	}
	
	/**
	 * Create or Get a type for given schema object.
	 * @param _package
	 * @param schema
	 * @return
	 * @throws ClassNotFoundException
	 */
	public JType createOrGetType(JCodeModel codeModel, ObjectSchema schema) throws ClassNotFoundException {
		// The purpose of the driver is to do all of the recursion for the handlers
		JType superType = codeModel._ref(Object.class);
		if (schema.getExtends() != null) {
			superType = createOrGetType(codeModel, schema.getExtends());
		}
		JType[] implementsArray = null;
		if(schema.getImplements() != null){
			implementsArray = new JType[schema.getImplements().length];
			for(int i=0; i<schema.getImplements().length; i++){
				implementsArray[i] = createOrGetType(codeModel, schema.getImplements()[i]);
			}
		}
		JType arrayType = null;
		if( schema.getItems() != null){
			arrayType = createOrGetType(codeModel, schema.getItems());
		}
		JType keyType = null;
		if (schema.getKey() != null) {
			keyType = createOrGetType(codeModel, schema.getKey());
		}
		JType valueType = null;
		if (schema.getValue() != null) {
			valueType = createOrGetType(codeModel, schema.getValue());
		}
		// Let the handler do most of the work.
		return factory.getTypeCreatorHandler().handelCreateType(codeModel, schema, superType, arrayType, keyType, valueType, implementsArray);
	}
	
	/**
	 * Build up a map of all identified schemas in the list.
	 * Note: This will Recursively walk all sub-schemas of each object.
	 * @param list
	 * @return
	 * @throws IllegalArgumentException when duplicate ids are found.
	 */
	protected static Map<String, ObjectSchema> registerAllIdentifiedObjectSchemas(List<ObjectSchema> list){
		Map<String, ObjectSchema> map = new HashMap<String, ObjectSchema>();
		// Walk over all schemas and build up the map.
		for(ObjectSchema schema: list){
			registerAllIdentifiedObjectSchemas(map, schema);
		}
		return map;
	}
	
	/**
	 * Recursively walk all objects
	 * @param map
	 * @param schemas
	 */
	protected static void registerAllIdentifiedObjectSchemas(Map<String, ObjectSchema> map, ObjectSchema schema){
		// first add this object to the map if it has an id
		if(schema.getId() != null){
			ObjectSchema duplicate = map.put(schema.getId(), schema);
			if(duplicate != null) throw new IllegalArgumentException("More than one schema was found with id="+duplicate.getId());
		}
		// Now add all sub-schemas
		Iterator<ObjectSchema> it = schema.getSubSchemaIterator();
		while(it.hasNext()){
			ObjectSchema sub = it.next();
			registerAllIdentifiedObjectSchemas(map, sub);
		}
	}
	
	/**
	 * A schema can be a reference to another schema.  This function will find all references and replace them with the
	 * actual schema.  Will throw
	 * @param map
	 * @param list
	 * @throws IllegalArgumentException if a reference cannot be resolved.
	 */
	protected static List<ObjectSchema> findAndReplaceAllReferencesSchemas(Map<String, ObjectSchema> map, List<ObjectSchema> list){
		List<ObjectSchema> results = new ArrayList<ObjectSchema>();
		for(ObjectSchema schema: list){
			// If this schema is a reference then replace it.
			schema = replaceRefrence(map, schema, schema);
			results.add(schema);
			// Replace all references in this schema
			recursiveFindAndReplaceAllReferencesSchemas(map, schema);
		}
		return results;
	}
	/**
	 * @param map
	 * @param schema
	 */
	protected static void recursiveFindAndReplaceAllReferencesSchemas(Map<String, ObjectSchema> map, ObjectSchema schema){
		// First replace for each child
		Iterator<ObjectSchema> it = schema.getSubSchemaIterator();
		while(it.hasNext()){
			ObjectSchema sub = it.next();
			recursiveFindAndReplaceAllReferencesSchemas(map, sub);
		}
		// Now do the replace for this object
		findAndReplaceAllReferencesSchemas(map,schema);
	}
	
	/**
	 * Find and replace all references found in this schema.
	 * @param map
	 * @param schema
	 */
	protected static void findAndReplaceAllReferencesSchemas(Map<String, ObjectSchema> map, ObjectSchema schema){
		// Properties
		if(schema.getProperties() != null){
			schema.setProperties(findAndReplaceAllReferencesSchemas(map, schema.getProperties(), schema));
		}
		// Additional
		if(schema.getAdditionalProperties() != null){
			schema.setAdditionalProperties(findAndReplaceAllReferencesSchemas(map, schema.getAdditionalProperties(), schema));
		}
		// Items
		if(schema.getItems() != null){
			schema.setItems(replaceRefrence(map, schema.getItems(), schema));
		}
		// Key
		if (schema.getKey() != null) {
			schema.setKey(replaceRefrence(map, schema.getKey(), schema));
		}
		// Value
		if (schema.getValue() != null) {
			schema.setValue(replaceRefrence(map, schema.getValue(), schema));
		}
		// AdditionItems
		if(schema.getAdditionalItems() != null){
			schema.setAdditionalItems(replaceRefrence(map, schema.getAdditionalItems(), schema));
		}
		// Extends
		if(schema.getExtends() != null){
			schema.setExtends(replaceRefrence(map, schema.getExtends(), schema));
		}
		// Implements
		if(schema.getImplements() != null){
			for(int i=0; i<schema.getImplements().length; i++){
				schema.getImplements()[i] = replaceRefrence(map, schema.getImplements()[i], schema);
			}
		}
	}
	
	/**
	 * Rebuild the passed map replacing all references.
	 * @param registry
	 * @param toCheck
	 * @param self
	 * @return
	 */
	protected static LinkedHashMap<String, ObjectSchema> findAndReplaceAllReferencesSchemas(Map<String, ObjectSchema> registry, Map<String, ObjectSchema> toCheck, ObjectSchema self){
		LinkedHashMap<String, ObjectSchema> newMap = new LinkedHashMap<String, ObjectSchema>();
		Iterator<String> it = toCheck.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			ObjectSchema schema = toCheck.get(key);
			schema = replaceRefrence(registry, schema, self);
			newMap.put(key, schema);
		}
		return newMap;
	}
	
	/**
	 * If the passed object has a reference then it will be replaced either with self, or from the registry.
	 * @param registry
	 * @param toCheck
	 * @param self
	 * @return
	 */
	protected static ObjectSchema replaceRefrence(Map<String, ObjectSchema> registry, ObjectSchema toCheck, ObjectSchema self) {
		// Nothing to do if it is not a reference.
		if (toCheck.getRef() == null)
			return toCheck;
		// Is it a self reference?
		if (ObjectSchema.SELF_REFERENCE.equals(toCheck.getRef().toString())) {
			return self;
		} else {
			// Find it in the registry
			ObjectSchema fromRegistry = registry.get(toCheck.getRef());
			if (fromRegistry == null) throw new IllegalArgumentException("Cannot find the referenced schema: "+ toCheck.getRef());
			return fromRegistry;
		}
	}




}
