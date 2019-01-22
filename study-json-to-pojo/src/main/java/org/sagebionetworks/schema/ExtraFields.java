package org.sagebionetworks.schema;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;

/**
 * Helper to 
 * @author John
 *
 */
public class ExtraFields {
	
	/**
	 * Create a map containing the all values from the provided adapter with a keys that are not included in the provided fieldNamesToExclude.
	 * 
	 * @param adapter
	 * @param fieldNamesToExclude All field names that should be excluded from the map.
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static Map<String, Object> createExtraFieldsMap(JSONObjectAdapter adapter, String...fieldNamesToExclude) throws JSONObjectAdapterException{
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		// start with all fields in the map.
		Iterator<String> keyIt = adapter.keys();
		while(keyIt.hasNext()) {
			String key = keyIt.next();
			map.put(key, adapter.get(key));
		}
		// remove the excluded fields.
		if(fieldNamesToExclude != null) {
			for(String fieldName: fieldNamesToExclude) {
				map.remove(fieldName);
			}
		}
		return map;
	}

}
