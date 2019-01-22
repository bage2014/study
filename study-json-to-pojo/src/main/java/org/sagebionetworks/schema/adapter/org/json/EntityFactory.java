package org.sagebionetworks.schema.adapter.org.json;

import org.json.JSONObject;
import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.adapter.JSONEntity;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;

/**
 * Helper for generating JSONEntity using adapters.
 * @author jmhill
 *
 */
public class EntityFactory {
	
	/**
	 * Create a JSON String representing the passed entity.
	 * @param entity
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static String createJSONStringForEntity(JSONEntity entity) throws JSONObjectAdapterException{
		if(entity == null) throw new IllegalArgumentException("Entity cannot be null");
		JSONObjectAdapterImpl adapter = writeEntityToAdapter(entity);
		return adapter.toJSONString();
	}
	
	/**
	 * Create a JSON String representing the passed entity.
	 * @param entity
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static JSONObject createJSONObjectForEntity(JSONEntity entity) throws JSONObjectAdapterException{
		if(entity == null) throw new IllegalArgumentException("Entity cannot be null");
		JSONObjectAdapterImpl adapter = writeEntityToAdapter(entity);
		return adapter.wrapped;
	}

	/**
	 * Write the passed entity to an adapter.
	 * @param entity
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	private static JSONObjectAdapterImpl writeEntityToAdapter(JSONEntity entity)
			throws JSONObjectAdapterException {
		JSONObjectAdapterImpl adapter = new JSONObjectAdapterImpl();
		entity.writeToJSONObject(adapter);
		return adapter;
	}
	
	/**
	 * Create an entity from a JSON String.
	 * @param <T>
	 * @param jsonEntity
	 * @param clazz
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static <T extends JSONEntity> T createEntityFromJSONString(String jsonString, Class<? extends T> clazz) throws JSONObjectAdapterException{
		if(jsonString == null) throw new IllegalArgumentException("JSON string cannot be null");
		if(clazz == null) throw new IllegalArgumentException("JSONEntity class cannot be null");
		// First create an adapter with the datat
		JSONObjectAdapter adapter = new JSONObjectAdapterImpl(jsonString);
		return createEntityFromAdapter(clazz, adapter); 
	}
	
	
	/**
	 * Create an entity from a JSON String.
	 * @param <T>
	 * @param jsonEntity
	 * @param clazz
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static <T extends JSONEntity> T createEntityFromJSONObject(JSONObject jsonEntity, Class<? extends T> clazz) throws JSONObjectAdapterException{
		if(jsonEntity == null) throw new IllegalArgumentException("JSONObject cannot be null");
		if(clazz == null) throw new IllegalArgumentException("JSONEntity class cannot be null");
		// First create an adapter with the datat
		JSONObjectAdapter adapter = new JSONObjectAdapterImpl(jsonEntity);
		return createEntityFromAdapter(clazz, adapter); 
	}

	/**
	 * Given an adapter and class, create an instance of the entity.
	 * @param <T>
	 * @param clazz
	 * @param adapter
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	private static <T extends JSONEntity> T createEntityFromAdapter(Class<? extends T> clazz, JSONObjectAdapter adapter) throws JSONObjectAdapterException {
		// Now create a new instance of the class
		try {
			T newInstance = null;
			if(clazz.isInterface()){
				// we need to determine the concrete type
				String concreteType = adapter.getString(ObjectSchema.CONCRETE_TYPE);
				// Use the concrete type to instanciate the object.
				newInstance = (T)Class.forName(concreteType).newInstance();
			}else{
				newInstance = clazz.newInstance();
			}
			newInstance.initializeFromJSONObject(adapter);
			return newInstance;
		} catch (Exception e) {
			throw new JSONObjectAdapterException(e);
		}
	}

}
