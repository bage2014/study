package org.sagebionetworks.schema.adapter;

/**
 * The adapter interfaces allow us to create model objects that can work with JSON
 * without depending on a specific implementation.
 * @author John
 *
 */
public class JSONObjectAdapterException extends Exception {

	/**
	 * Auto-generated
	 */
	private static final long serialVersionUID = -221157297330088851L;

	public JSONObjectAdapterException() {
		super();
	}

	public JSONObjectAdapterException(String message, Throwable cause) {
		super(message, cause);
	}

	public JSONObjectAdapterException(String message) {
		super(message);
	}

	public JSONObjectAdapterException(Throwable cause) {
		super(cause);
	}

}
