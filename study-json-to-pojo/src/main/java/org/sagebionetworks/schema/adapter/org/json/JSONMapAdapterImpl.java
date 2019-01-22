package org.sagebionetworks.schema.adapter.org.json;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sagebionetworks.schema.FORMAT;
import org.sagebionetworks.schema.adapter.JSONArrayAdapter;
import org.sagebionetworks.schema.adapter.JSONMapAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.binary.Base64;

/**
 * An org.json.JSONObject Implementation of JSONObjectAdapter.
 * 
 * @author John
 * 
 */
public class JSONMapAdapterImpl extends AdapterFactoryImpl implements JSONMapAdapter {

	protected JSONArray wrapped;
	private Map<Object, JSONObject> wrappedMap = new HashMap<Object, JSONObject>();

	public JSONMapAdapterImpl() {
		wrapped = new JSONArray();
	}

	public JSONMapAdapterImpl(JSONArray array) throws JSONObjectAdapterException {
		wrapped = array;
		unwrap();
	}

	public JSONMapAdapterImpl(String jsonString) throws JSONObjectAdapterException {
		try {
			wrapped = new JSONArray(jsonString);
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
		unwrap();
	}

	private void unwrap() throws JSONObjectAdapterException {
		try {
			for (int i = 0; i < wrapped.length(); i++) {
				JSONObject jsonObject = wrapped.getJSONObject(i);
				Object key = jsonObject.get("key");
				wrappedMap.put(key, jsonObject);
			}
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public boolean getBoolean(Object key) throws JSONObjectAdapterException {
		try {
			return wrappedMap.get(key).getBoolean("value");
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public double getDouble(Object key) throws JSONObjectAdapterException {
		try {
			return wrappedMap.get(key).getDouble("value");
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public int getInt(Object key) throws JSONObjectAdapterException {
		try {
			return wrappedMap.get(key).getInt("value");
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONArrayAdapter getJSONArray(Object key) throws JSONObjectAdapterException {
		try {
			return new JSONArrayAdapterImpl(wrappedMap.get(key).getJSONArray("value"));
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public long getLong(Object key) throws JSONObjectAdapterException {
		try {
			return wrappedMap.get(key).getLong("value");
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public String getString(Object key) throws JSONObjectAdapterException {
		try {
			return wrappedMap.get(key).getString("value");
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public Object get(Object key) throws JSONObjectAdapterException {
		try {
			Object result = wrappedMap.get(key).get("value");
			if (JSONObject.NULL == result)
				return null;
			return result;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public boolean isNull(Object key) {
		return wrappedMap.get(key).isNull("value");
	}

	@Override
	public int length() {
		return wrapped.length();
	}

	@Override
	public JSONObjectAdapter getJSONObject(Object key) throws JSONObjectAdapterException {
		try {
			return new JSONObjectAdapterImpl(wrappedMap.get(key).getJSONObject("value"));
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public String toJSONString() {
		return wrapped.toString();
	}

	private void doPut(Object key, Object value) throws JSONObjectAdapterException {
		try {
			JSONObject entry = new JSONObject();
			entry.put("key", key);
			entry.put("value", value);
			wrapped.put(wrapped.length(), entry);
			wrappedMap.put(key, entry);
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONMapAdapter put(Object key, JSONArrayAdapter value) throws JSONObjectAdapterException {
		JSONMapAdapterImpl impl = (JSONMapAdapterImpl) value;
		doPut(key, impl.wrapped);
		return this;
	}

	@Override
	public JSONMapAdapter put(Object key, JSONObjectAdapter value) throws JSONObjectAdapterException {
		JSONObjectAdapterImpl impl = (JSONObjectAdapterImpl) value;
		doPut(key, impl.wrapped);
		return this;
	}

	@Override
	public JSONMapAdapter put(Object key, String value) throws JSONObjectAdapterException {
		doPut(key, value);
		return this;
	}

	@Override
	public JSONMapAdapter put(Object key, long value) throws JSONObjectAdapterException {
		doPut(key, value);
		return this;
	}

	@Override
	public JSONMapAdapter put(Object key, double value) throws JSONObjectAdapterException {
		doPut(key, value);
		return this;
	}

	@Override
	public JSONMapAdapter put(Object key, boolean value) throws JSONObjectAdapterException {
		doPut(key, value);
		return this;
	}

	@Override
	public JSONMapAdapter put(Object key, int value) throws JSONObjectAdapterException {
		doPut(key, value);
		return this;
	}

	@Override
	public JSONMapAdapter putNull(Object key) throws JSONObjectAdapterException {
		doPut(key, JSONObject.NULL);
		return this;
	}

	@Override
	public String toString() {
		return wrapped.toString();
	}

	@Override
	public int hashCode() {
		return wrapped.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return wrapped.equals(obj);
	}

	@Override
	public String convertDateToString(FORMAT format, Date toFormat) {
		return JsonDateUtils.convertDateToString(format, toFormat);
	}

	@Override
	public Date convertStringToDate(FORMAT format, String toFormat) {
		return JsonDateUtils.convertStringToDate(format, toFormat);
	}

	@Override
	public Date getDate(Object key) throws JSONObjectAdapterException {
		// Get the string value
		long longValue = getLong(key);
		return new Date(longValue);
	}

	@Override
	public JSONMapAdapter put(Object key, Date date) throws JSONObjectAdapterException {
		if (date == null)
			throw new IllegalArgumentException("Date cannot be null");
		// Stored as a long
		return put(key, date.getTime());
	}

	@Override
	public JSONMapAdapter put(Object key, byte[] value) throws JSONObjectAdapterException {
		// Base64 encode the byte array
		try {
			byte[] encoded = Base64.encodeBase64(value);
			String stringValue = new String(encoded, "UTF-8");
			return put(key, stringValue);
		} catch (UnsupportedEncodingException e) {
			throw new JSONObjectAdapterException(e);
		}

	}

	@Override
	public byte[] getBinary(Object key) throws JSONObjectAdapterException {
		try {
			// Get the string value
			String base64String = getString(key);
			return Base64.decodeBase64(base64String.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONMapAdapter createNewMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONMapAdapter createNewMap(String json) throws JSONObjectAdapterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Object> keys() {
		return wrappedMap.keySet();
	}
}
