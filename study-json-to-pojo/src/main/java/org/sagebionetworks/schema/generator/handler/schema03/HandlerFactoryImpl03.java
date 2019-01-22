package org.sagebionetworks.schema.generator.handler.schema03;

import org.sagebionetworks.schema.generator.handler.HandlerFactory;
import org.sagebionetworks.schema.generator.handler.HashAndEqualsHandler;
import org.sagebionetworks.schema.generator.handler.JSONMarshalingHandler;
import org.sagebionetworks.schema.generator.handler.PropertyHandler;
import org.sagebionetworks.schema.generator.handler.ToStringHandler;
import org.sagebionetworks.schema.generator.handler.TypeCreatorHandler;

/**
 * An implementation of the handler factor for the 03 version of the JSON schema
 *  * @see <a
 *      href="http://tools.ietf.org/html/draft-zyp-json-schema-03">http://tools.ietf.org/html/draft-zyp-json-schema-03</a>
 * @author jmhill
 *
 */
public class HandlerFactoryImpl03 implements HandlerFactory {

	@Override
	public TypeCreatorHandler getTypeCreatorHandler() {
		return new TypeCreatorHandlerImpl03();
	}

	@Override
	public PropertyHandler getPropertyHandler() {
		return new PropertyHandlerImpl03();
	}

	@Override
	public JSONMarshalingHandler getJSONMArshalingHandler() {
		return new JSONMarshalingHandlerImpl03();
	}

	@Override
	public HashAndEqualsHandler getHashAndEqualsHandler() {
		return new HashAndEqualsHandlerImpl03();
	}
	
	@Override
	public ToStringHandler getToStringHandler() {
		return new ToStringHandlerImpl03();
	}
}
