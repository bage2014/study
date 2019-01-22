package org.sagebionetworks.schema.adapter.org.json;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
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
public class JSONObjectAdapterImpl extends AdapterFactoryImpl implements JSONObjectAdapter {
	
	protected JSONObject wrapped;
	
	public JSONObjectAdapterImpl(){
		wrapped = new JSONObject();
	}
	
	/**
	 * Create a new adapter from a JSON string
	 * @param json
	 * @throws JSONObjectAdapterException 
	 */
	public JSONObjectAdapterImpl(String json) throws JSONObjectAdapterException{
		try {
			wrapped = new JSONObject(json);
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}
	
	public JSONObjectAdapterImpl(JSONObject jsonObject) {
		wrapped = jsonObject;
	}

	@Override
	public String toJSONString() {
		return wrapped.toString();
	}
	
	@Override
	public Object get(String key) throws JSONObjectAdapterException {
		try {
			Object result = wrapped.get(key);
			if(JSONObject.NULL == result) return null;
			if(result instanceof JSONObject){
				return new JSONObjectAdapterImpl((JSONObject) result);
			}else if(result instanceof JSONArray){
				return new JSONArrayAdapterImpl((JSONArray) result);
			}
			return result;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public String getString(String key) throws JSONObjectAdapterException {
		try {
			return wrapped.getString(key);
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}


	@Override
	public long getLong(String key) throws JSONObjectAdapterException {
		try {
			return wrapped.getLong(key);
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public boolean getBoolean(String key) throws JSONObjectAdapterException {
		try {
			return wrapped.getBoolean(key);
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public double getDouble(String key) throws JSONObjectAdapterException {
		try {
			return wrapped.getDouble(key);
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public int getInt(String key) throws JSONObjectAdapterException {
		try {
			return wrapped.getInt(key);
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONArrayAdapter getJSONArray(String key) throws JSONObjectAdapterException {
		try {
			return new JSONArrayAdapterImpl(wrapped.getJSONArray(key));
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONMapAdapter getJSONMap(String key) throws JSONObjectAdapterException {
		try {
			return new JSONMapAdapterImpl(wrapped.getJSONArray(key));
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONObjectAdapter getJSONObject(String key)
			throws JSONObjectAdapterException {
		try {
			return new JSONObjectAdapterImpl(wrapped.getJSONObject(key));
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public boolean has(String key) {
		return wrapped.has(key);
	}

	@Override
	public boolean isNull(String key) {
		return wrapped.isNull(key);
	}

	@Override
	public JSONObjectAdapter put(String key, JSONObjectAdapter value)
			throws JSONObjectAdapterException {
		JSONObjectAdapterImpl impl = (JSONObjectAdapterImpl) value;
		try {
			wrapped.put(key, impl.wrapped);
			return this;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}


	@Override
	public Iterator<String> keys() {
		return wrapped.keys();
	}

	@Override
	public JSONObjectAdapter put(String key, boolean value)
			throws JSONObjectAdapterException {
		try {
			wrapped.put(key, value);
			return this;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONObjectAdapter put(String key, String value)
			throws JSONObjectAdapterException {
		try {
			wrapped.put(key, value);
			return this;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}
	
	@Override
	public JSONObjectAdapter putNull(String key)
			throws JSONObjectAdapterException {
		try {
			wrapped.put(key, JSONObject.NULL);
			return this;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONObjectAdapter put(String key, double value)
			throws JSONObjectAdapterException {
		try {
			if (Double.isNaN(value) || Double.isInfinite(value)) {
				wrapped.put(key, Double.toString(value));
			} else {
				wrapped.put(key, value);
			}
			return this;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONObjectAdapter put(String key, int value)
			throws JSONObjectAdapterException {
		try {
			wrapped.put(key, value);
			return this;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONObjectAdapter put(String key, long value)
			throws JSONObjectAdapterException {
		try {
			wrapped.put(key, value);
			return this;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONObjectAdapter put(String key, JSONArrayAdapter value)
			throws JSONObjectAdapterException {
		JSONArrayAdapterImpl impl = (JSONArrayAdapterImpl) value;
		try {
			wrapped.put(key, impl.wrapped);
			return this;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONObjectAdapter put(String key, JSONMapAdapter value) throws JSONObjectAdapterException {
		JSONMapAdapterImpl impl = (JSONMapAdapterImpl) value;
		try {
			wrapped.put(key, impl.wrapped);
			return this;
		} catch (JSONException e) {
			throw new JSONObjectAdapterException(e);
		}
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

	/**
	 * Method to validate a regular expression string against a pattern.
	 */
	public boolean validatePatternProperty(String pattern, String property){
		if (pattern == null){
			throw new IllegalArgumentException("can not validatePatternProperty for property " 
					+ property + " because pattern is null");
		}
		if (property == null){
			throw new IllegalArgumentException("can not validatePatternProperty for pattern "
					+ pattern + "because property is null");
		}
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(property);
		return m.matches();
	}

	@Override
	public boolean validateURI(String uri) throws JSONObjectAdapterException {
		try {
			// Let Java validate the URI.
			new URI(uri);
			return true;
		} catch (URISyntaxException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

	@Override
	public JSONObjectAdapter put(String key, Date date) throws JSONObjectAdapterException {
		if(key == null) throw new IllegalArgumentException("Key cannot be null");
		if(date == null) throw new IllegalArgumentException("Date cannot be null");
		// first convert it to a date string
		return put(key, date.getTime());
	}

	@Override
	public Date getDate(String key) throws JSONObjectAdapterException {
		// Get the string value
		long longValue = getLong(key);
		return new Date(longValue);
	}

	@Override
	public JSONObjectAdapter put(String key, byte[] value)	throws JSONObjectAdapterException {
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
	public byte[] getBinary(String key) throws JSONObjectAdapterException {
		try {
			// Get the string value
			String base64String = getString(key);
			return Base64.decodeBase64(base64String.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new JSONObjectAdapterException(e);
		}
	}

}
