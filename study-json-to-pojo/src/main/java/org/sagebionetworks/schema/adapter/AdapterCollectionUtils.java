package org.sagebionetworks.schema.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.sagebionetworks.schema.FORMAT;


/**
 * Utilities for reading and writing collections to adapters.
 * @author John
 *
 */
public class AdapterCollectionUtils {
	
	public static void writeToObject(JSONObjectAdapter adapter, Map<String, Object> map) throws JSONObjectAdapterException{
		for(String key: map.keySet()){
			Object value = map.get(key);
			if(value instanceof String){
				adapter.put(key,(String)value);
			}else if(value instanceof Long){
				adapter.put(key,(Long)value);
			}else if(value instanceof Double){
				adapter.put(key,(Double)value);
			}else if(value instanceof Integer){
				adapter.put(key,(Integer)value);
			}else if(value instanceof Boolean){
				adapter.put(key,(Boolean)value);
			}else if(value == null){
				adapter.putNull(key);
			}else if(value instanceof Iterable ){
				Iterable itable = (Iterable) value;
				JSONArrayAdapter newArray = adapter.createNewArray();
				adapter.put(key, newArray);
				writeToArray(newArray, itable);
			}else if(value instanceof String[] ){
				String[] itable = (String[]) value;
				JSONArrayAdapter newArray = adapter.createNewArray();
				adapter.put(key, newArray);
				writeToArray(newArray, itable);
			}else if(value instanceof Long[] ){
				Long[] itable = (Long[]) value;
				JSONArrayAdapter newArray = adapter.createNewArray();
				adapter.put(key, newArray);
				writeToArray(newArray, itable);
			}else if(value instanceof Double[] ){
				Double[] itable = (Double[]) value;
				JSONArrayAdapter newArray = adapter.createNewArray();
				adapter.put(key, newArray);
				writeToArray(newArray, itable);
			}else if(value instanceof Boolean[] ){
				Boolean[] itable = (Boolean[]) value;
				JSONArrayAdapter newArray = adapter.createNewArray();
				adapter.put(key, newArray);
				writeToArray(newArray, itable);
			} else if (value instanceof JSONObjectAdapter) {
				adapter.put(key, (JSONObjectAdapter) value);
			} else if (value instanceof JSONArrayAdapter) {
				adapter.put(key, (JSONArrayAdapter) value);
			} else if (value instanceof JSONMapAdapter) {
				adapter.put(key, (JSONMapAdapter) value);
			}else{
				throw new IllegalArgumentException("Unknown type: "+value.getClass().getName());
			}
		}
	}
	
	public static void writeToArray(JSONArrayAdapter array, Iterable iterable) throws JSONObjectAdapterException{
		int index = 0;
		for(Object value: iterable){
			if(value instanceof String){
				array.put(index,(String)value);
			}else if(value instanceof Long){
				array.put(index,(Long)value);
			}else if(value instanceof Double){
				array.put(index,(Double)value);
			}else if(value instanceof Integer){
				array.put(index,(Integer)value);
			}else if(value instanceof Boolean){
				array.put(index,(Boolean)value);
			}else if(value == null){
				array.putNull(index);
			}else if(value instanceof Map ){
				Map<String, Object> map = (Map<String, Object>) value;
				JSONObjectAdapter object = array.createNew();
				array.put(index, object);
				writeToObject(object, map);
			}else if(value instanceof Iterable ){
				Iterable itable = (Iterable) value;
				JSONArrayAdapter newArray = array.createNewArray();
				array.put(index, newArray);
				writeToArray(newArray, itable);
			}else if(value instanceof String[] ){
				String[] itable = (String[]) value;
				JSONArrayAdapter newArray = array.createNewArray();
				array.put(index, newArray);
				writeToArray(newArray, itable);
			}else if(value instanceof Long[] ){
				Long[] itable = (Long[]) value;
				JSONArrayAdapter newArray = array.createNewArray();
				array.put(index, newArray);
				writeToArray(newArray, itable);
			}else if(value instanceof Double[] ){
				Double[] itable = (Double[]) value;
				JSONArrayAdapter newArray = array.createNewArray();
				array.put(index, newArray);
				writeToArray(newArray, itable);
			}else if(value instanceof Boolean[] ){
				Boolean[] itable = (Boolean[]) value;
				JSONArrayAdapter newArray = array.createNewArray();
				array.put(index, newArray);
				writeToArray(newArray, itable);
			}else{
				throw new IllegalArgumentException("Unknown type: "+value.getClass().getName());
			}
			index++;
		}
	}
	
	public static void writeToArray(JSONArrayAdapter newArray, Boolean[] array) throws JSONObjectAdapterException {
		for(int i=0; i<array.length; i++){
			newArray.put(i, array[i]);
		}
	}

	public static void writeToArray(JSONArrayAdapter newArray, Double[] array) throws JSONObjectAdapterException {
		for(int i=0; i<array.length; i++){
			newArray.put(i, array[i]);
		}
	}

	public static void writeToArray(JSONArrayAdapter newArray, Long[] array) throws JSONObjectAdapterException {
		for(int i=0; i<array.length; i++){
			newArray.put(i, array[i]);
		}
	}

	public static void writeToArray(JSONArrayAdapter newArray, String[] array) throws JSONObjectAdapterException {
		for(int i=0; i<array.length; i++){
			newArray.put(i, array[i]);
		}
	}
	
	/**
	 * Get a string array from an adapter.
	 * @param newArray
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static List<String> readListOfStrings(JSONArrayAdapter newArray) throws JSONObjectAdapterException{
		List<String> restuls = new ArrayList<String>();
		for(int i=0; i<newArray.length(); i++){
			restuls.add(newArray.getString(i));
		}
		return restuls;
	}
	
	public static List<Map<String, Object>> readListOfMaps(JSONArrayAdapter newArray) throws JSONObjectAdapterException{
		List<Map<String, Object>> restuls = new ArrayList<Map<String, Object>>();
		for(int i=0; i<newArray.length(); i++){
			JSONObjectAdapter object  = newArray.getJSONObject(i);
			restuls.add(readMapFromObject(object));
		}
		return restuls;
	}
	
	public static Object readObjectFromArray(JSONArrayAdapter array) throws JSONObjectAdapterException{
		if(array.length() < 1) return null;
		Object peak = array.get(0);
		if(peak instanceof String){
			return readStringListFromArray(array);
		}else if(peak instanceof Long){
			return readLongListFromArray(array);
		}else if(peak instanceof Double){
			return readDoubleListFromArray(array);
		}else if(peak instanceof Integer){
			return readIntegerListFromArray(array);
		}else if(peak instanceof Boolean){
			return readBooleanListFromArray(array);
		}else if(peak instanceof JSONObjectAdapter){
			return readListOfMap(array);
		}else{
			throw new IllegalArgumentException("Unsupported array sub type: "+peak.getClass().getName());
		}
	}
	
	public static List<Map<String, Object>> readListOfMap(JSONArrayAdapter array) throws JSONObjectAdapterException {
		List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
		for(int i=0; i<array.length(); i++){
			JSONObjectAdapter object = array.getJSONObject(i);
			Map<String, Object> map = readMapFromObject(object);
			results.add(map);
		}
		return results;
	}

	public static List<Boolean> readBooleanListFromArray(JSONArrayAdapter array) throws JSONObjectAdapterException {
		List<Boolean> results = new ArrayList<Boolean>();
		for(int i=0; i<array.length(); i++){
			results.add(array.getBoolean(i));
		}
		return results;
	}

	public static List<Integer> readIntegerListFromArray(JSONArrayAdapter array) throws JSONObjectAdapterException {
		List<Integer> results = new ArrayList<Integer>();
		for(int i=0; i<array.length(); i++){
			results.add(array.getInt(i));
		}
		return results;
	}

	public static List<Double> readDoubleListFromArray(JSONArrayAdapter array) throws JSONObjectAdapterException {
		List<Double> results = new ArrayList<Double>();
		for(int i=0; i<array.length(); i++){
			results.add(array.getDouble(i));
		}
		return results;
	}

	public static List<Long> readLongListFromArray(JSONArrayAdapter array) throws JSONObjectAdapterException {
		List<Long> results = new ArrayList<Long>();
		for(int i=0; i<array.length(); i++){
			results.add(array.getLong(i));
		}
		return results;
	}

	public static List<String> readStringListFromArray(JSONArrayAdapter array) throws JSONObjectAdapterException {
		List<String> results = new ArrayList<String>();
		for(int i=0; i<array.length(); i++){
			results.add(array.getString(i));
		}
		return results;
	}

	/**
	 * Read
	 * @param adapter
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static Map<String, Object> readMapFromObject(JSONObjectAdapter adapter) throws JSONObjectAdapterException{
		Map<String, Object> results = new HashMap<String, Object>();
		Iterator<String> keyIt = adapter.keys();
		while(keyIt.hasNext()){
			String key = keyIt.next();
			Object value = adapter.get(key);
			if(value instanceof JSONObjectAdapter){
				Map<String, Object> subMap = readMapFromObject((JSONObjectAdapter)value);
				results.put(key, subMap);
			}else if(value instanceof JSONArrayAdapter){
				results.put(key, readObjectFromArray((JSONArrayAdapter)value));
			}else{
				results.put(key, value);
			}
		}
		return results;
	}
	
	/**
	 * Create Map<String, Collection<String>> from a JSONObjectAdapter.
	 * @param object
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static <T> Map<String, List<T>> createMapOfCollection(JSONObjectAdapter object, Class<? extends T> clazz) throws JSONObjectAdapterException{
		HashMap<String, List<T>> map = new HashMap<String, List<T>>();
		Iterator<String> it = object.keys();
		while(it.hasNext()){
			String key = it.next();
			if(object.isNull(key)){
				map.put(key, null);
			}else{
				JSONArrayAdapter array = object.getJSONArray(key);
				List<T> values = AdapterCollectionUtils.readListFromArray(array, clazz);
				map.put(key, values);
			}
		}
		return map;
	}
	
	/**
	 * Read a List<T> from JSONArrayAdapter
	 * @param <T>
	 * @param array
	 * @param clazz
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static <T> List<T> readListFromArray(JSONArrayAdapter array, Class<? extends T> clazz) throws JSONObjectAdapterException {
		List<T> results = new ArrayList<T>();
		for(int i=0; i<array.length(); i++){
			if(array.isNull(i)){
				results.add(null);
			}else if(String.class == clazz){
				String string = array.getString(i);
				results.add((T)string);
			}else if(Double.class == clazz){
				Double value = array.getDouble(i);
				results.add((T)value);
			}else if(Long.class == clazz){
				Long value = array.getLong(i);
				results.add((T)value);
			}else if(Date.class == clazz){
				Date value = array.getDate(i);
				results.add((T)value);
			}else if(byte[].class == clazz){
				byte[] value = array.getBinary(i);
				results.add((T)value);
			}else{
				throw new IllegalArgumentException("Unknown type: "+clazz);
			}
		}
		return results;
	}
	
	/**
	 * Write a Map<String, Collection<String>> to the passed JSONObjectAdapter.
	 * @param object
	 * @param map
	 * @throws JSONObjectAdapterException
	 */
	public static <T> void writeToAdapter(JSONObjectAdapter object, Map<String, List<T>> map, Class<? extends T> clazz) throws JSONObjectAdapterException{
		for(String key: map.keySet()){
			List<T> values = map.get(key);
			if(values == null){
				object.putNull(key);
			}else{
				JSONArrayAdapter array = object.createNewArray();
				AdapterCollectionUtils.writeToArray(array, values, clazz);
				object.put(key, array);
			}

		}
	}
	
	/**
	 * Write a list JSONArrayAdapter
	 * @param <T>
	 * @param newArray
	 * @param list
	 * @param clazz
	 * @throws JSONObjectAdapterException
	 */
	public static <T> void writeToArray(JSONArrayAdapter newArray, List<T> list, Class<? extends T> clazz) throws JSONObjectAdapterException {
		if(list != null){
			for(int i=0; i<list.size(); i++){
				T value = list.get(i);
				if(value == null){
					newArray.putNull(i);
				}else if(clazz == String.class){
					newArray.put(i, (String)list.get(i));
				}else if(Double.class == clazz){
					newArray.put(i, (Double)list.get(i));
				}else if(Long.class == clazz){
					newArray.put(i, (Long)list.get(i));
				}else if(Date.class == clazz){
					Date date = (Date) list.get(i);
					newArray.put(i, date);
				}else if(byte[].class == clazz){
					byte[] byteArray = (byte[]) list.get(i);
					newArray.put(i, byteArray);
				}else{
					throw new IllegalArgumentException("Unknown type: "+clazz);
				}
			}		
		}
	}


}
