package org.sagebionetworks.schema.adapter.org.json;

import org.sagebionetworks.schema.adapter.AdapterFactory;
import org.sagebionetworks.schema.adapter.JSONArrayAdapter;
import org.sagebionetworks.schema.adapter.JSONMapAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;

/**
 * The json.org implementation of AdapterFactory.
 * 
 * @author John
 *
 */
public class AdapterFactoryImpl implements AdapterFactory {

	@Override
	public JSONObjectAdapter createNew() {
		return new JSONObjectAdapterImpl();
	}

	@Override
	public JSONObjectAdapter createNew(String json)	throws JSONObjectAdapterException {
		return new JSONObjectAdapterImpl(json);
	}

	@Override
	public JSONArrayAdapter createNewArray() {
		return new JSONArrayAdapterImpl();
	}

	@Override
	public JSONArrayAdapter createNewArray(String json)	throws JSONObjectAdapterException {
		return new JSONArrayAdapterImpl(json);
	}

	@Override
	public JSONMapAdapter createNewMap() {
		return new JSONMapAdapterImpl();
	}

	@Override
	public JSONMapAdapter createNewMap(String json) throws JSONObjectAdapterException {
		return new JSONMapAdapterImpl(json);
	}
}
