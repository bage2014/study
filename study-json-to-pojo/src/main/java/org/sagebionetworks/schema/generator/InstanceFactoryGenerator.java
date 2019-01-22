package org.sagebionetworks.schema.generator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.TYPE;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

/**
 * Generates a factory that can be used to create new instances of a particular
 * interface. Each interface will have its own factory.
 * 
 * @author jmhill
 * 
 */
public class InstanceFactoryGenerator {

	public static final String INSTANCE_FACTORY_SUFFIX = "InstanceFactory";
	JCodeModel codeModel;
	List<ObjectSchema> allObjects;
	Map<String, JDefinedClass> interfaceMap = new HashMap<String, JDefinedClass>();
	Map<JDefinedClass, List<ObjectSchema>> implMap = new HashMap<JDefinedClass, List<ObjectSchema>>();
	Map<JDefinedClass, String> factoryForMap = new HashMap<JDefinedClass, String>();
	
	/**
	 * 
	 * @param codeModel The code model.
	 * @param allObjects This list should contain all schemas in the namespaces.
	 * 
	 */
	public InstanceFactoryGenerator(JCodeModel codeModel,
			List<ObjectSchema> allObjects) {
		super();
		this.codeModel = codeModel;
		this.allObjects = allObjects;
		initialize();
	}
	
	/**
	 * Build up each instance factory.
	 */
	private void initialize(){
		// For each interface build up a list of schemas that implement that interface.
		// A factory will be created for each of the interfaces.
		Map<String, List<ObjectSchema>> map = new HashMap<String, List<ObjectSchema>>();
		for(ObjectSchema schema: allObjects){
			// Skip interfaces
			if(TYPE.INTERFACE.equals(schema.getType())){
				continue;
			}
			// Look at each concrete type.
			List<String> implementsIds = getImplementsIds(schema);
			for(String implementsId: implementsIds){
				List<ObjectSchema> instanceList = map.get(implementsId);
				if(instanceList == null){
					instanceList = new LinkedList<ObjectSchema>();
					map.put(implementsId, instanceList);
				}
				// Add this schema to the list
				instanceList.add(schema);
			}
		}
		// Now create a factory for each interface
		for(String key: map.keySet()){
			List<ObjectSchema> instanceLis =  map.get(key);
			JDefinedClass factoryClass = RegisterGenerator.createClassFromFullName(codeModel, key+INSTANCE_FACTORY_SUFFIX);
			implMap.put(factoryClass, instanceLis);
			factoryForMap.put(factoryClass, key);
			// Map the interface to the factory
			this.interfaceMap.put(key, factoryClass);
		}
	}
	
	/**
	 * Get the factory that can be used to instantiate instances of the given interface.
	 * @param type ID
	 * @return
	 */
	public JDefinedClass getFactoryClass(String id) {
		JDefinedClass clazz = interfaceMap.get(id);
		if(clazz == null){
			throw new IllegalArgumentException("Cannot find a factory for: "+id+". Factories are only created for interfaces or abstract classes");
		}
		return clazz;
	}

	/**
	 * Get the factory that can be used to instantiate instances of the given interface.
	 * @param type
	 * @return
	 */
	public JDefinedClass getFactoryClass(JClass type) {
		return getFactoryClass(type.fullName());
	}
	
	/**
	 * Get the full package names of each interfaces this class implements.
	 * 
	 * @param schema
	 * @return
	 */
	private List<String> getImplementsIds(ObjectSchema schema){
		List<String> list = new LinkedList<String>();
		getImplementsIdsRecursive(list, schema.getImplements());
		return list;
	}
	
	/**
	 * Recursive walk of an array of interfaces.
	 * @param list
	 * @param schemas
	 */
	private static void getImplementsIdsRecursive(List<String> list, ObjectSchema[] schemas){
		if(schemas != null){
			for(ObjectSchema implementsSchema: schemas){
				getImplementsIdsRecursive(list, implementsSchema);
			}
		}
	}
	
	/**
	 * Recursive walk of an interface
	 * @param list
	 * @param schema
	 */
	private static void getImplementsIdsRecursive(List<String> list, ObjectSchema schema){	
		if(schema != null){
			// If this schema has schemas add them as well
			getImplementsIdsRecursive(list, schema.getImplements());
			// Add any interface this class directly implements.
			if(schema.getId() != null){
				list.add(schema.getId());
			}else{
				throw new IllegalArgumentException("Cannot find the ID of the interface schema: "+schema.getName());
			}
		}
	}
	/**
	 * After all POJOs have been created, call this method to create the factories.
	 */
	public void buildFactories(){
		// Now create a factory for each interface
		for(JDefinedClass factoryClass: implMap.keySet()){
			List<ObjectSchema> instanceLis =  implMap.get(factoryClass);
			String interfaceName = factoryForMap.get(factoryClass);
			RegisterGenerator.createRegister(codeModel, instanceLis, factoryClass, interfaceName);
		}
	}
	
	/**
	 * Get the IDs of the classes that the given factory can create.
	 * @param factory
	 * @return
	 */
	public Set<String> getImplementationIdsForFactory(JDefinedClass factoryClass){
		Set<String> list = new HashSet<String>();
		List<ObjectSchema> instanceList =  implMap.get(factoryClass);
		if(instanceList != null){
			for(ObjectSchema schema: instanceList){
				list.add(schema.getId());
			}
		}
		return list;
	}
	
}
