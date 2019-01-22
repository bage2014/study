package org.sagebionetworks.schema.generator.handler.schema03;

import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.generator.handler.PropertyHandler;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCommentPart;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

/**
 * 
 * Handles property creation for version 03 
 * @see <a
 *      href="http://tools.ietf.org/html/draft-zyp-json-schema-03">http://tools.ietf.org/html/draft-zyp-json-schema-03</a>
 *
 */
public class PropertyHandlerImpl03 implements PropertyHandler {

	@Override
	public JFieldVar createProperty(ObjectSchema propertySchema, JDefinedClass classType, String propertyName, JType propertyType) {
		// Create a private field for this property.
		JFieldVar field = null;
		if(!classType.isInterface()){
			// Create a field if this is not an interface
			if(ObjectSchema.CONCRETE_TYPE.equals(propertyName)){
				// Initialize the concrete type property with the full class name.
				field = classType.field(JMod.PRIVATE, propertyType, propertyName, classType.staticRef("class").invoke("getName"));
			}else{
				field = classType.field(JMod.PRIVATE, propertyType, propertyName);
			}
			// If there is a title then add it to the feild
			if(propertySchema.getTitle() != null){
				JDocComment doc = field.javadoc();
				doc.add(propertySchema.getTitle());
			}
		}

		// Create the getter and setter
		createGetter(propertySchema, classType, propertyName, propertyType, field);
		createSetter(propertySchema, classType, propertyName, propertyType, field);
		return field;
	}
	
	/**
	 * Create getter that matches the property name
	 * @param propertySchema
	 * @param classType
	 * @param propertyName
	 * @param propertyType
	 */
	private void createGetter(ObjectSchema propertySchema, JDefinedClass classType, String propertyName, JType propertyType, JFieldVar field){
		// Create the name
		String methodName = getterName(propertyName);
		JMethod method = classType.method(JMod.PUBLIC, propertyType, methodName);
		// Create a method body if this is not an interface
		if(!classType.isInterface()){
	        JBlock body = method.body();
	        body._return(field);
		}
        // Add the java doc
        JDocComment doc = method.javadoc();
        setCommentTileAndDescription(propertySchema, doc);
        JCommentPart part = doc.addReturn();
        part.add(propertyName);
	}
	
	/**
	 * Create the setter method.
	 * @param propertySchema
	 * @param classType
	 * @param propertyName
	 * @param propertyType
	 * @param field
	 */
	private void createSetter(ObjectSchema propertySchema, JDefinedClass classType, String propertyName, JType propertyType, JFieldVar field){
		String methodName = setterName(propertyName);
		JMethod method = classType.method(JMod.PUBLIC, classType.owner().VOID, methodName);
		// Add the parameter
		JVar param = method.param(propertyType, propertyName);
		// Create a method body if this is not an interface
		if(!classType.isInterface()){
	        JBlock body = method.body();
	        // Is this a required field?
	        if(propertySchema.isRequired() && !propertyType.isPrimitive()){
	        	// Since it is required throw an exception if set to null.
	        	JConditional conditional = body._if(param.eq(JExpr._null()));
	        	JInvocation invoke = JExpr._new(classType.owner().ref(IllegalArgumentException.class));
	        	invoke.arg(propertyName+" is required and cannot be set to null");
	        	conditional._then()._throw(invoke);
	        }
	        body.assign(JExpr._this().ref(field), param);
		}
        // Add the java doc
        JDocComment doc = method.javadoc();
        setCommentTileAndDescription(propertySchema, doc);
        JCommentPart part = doc.addParam(param);
	}

	/**
	 * Add a title and description if needed.
	 * @param propertySchema
	 * @param doc
	 */
	protected void setCommentTileAndDescription(ObjectSchema propertySchema, JDocComment doc) {
		if(propertySchema.getTitle() != null){
        	doc.add(propertySchema.getTitle());
        	doc.add("\n\n");
        }
        if(propertySchema.getDescription() != null){
        	doc.add(propertySchema.getDescription());
        	doc.add("\n\n");
        }
	}
	
	/**
	 * Creates a getter name from a field name.
	 * @param name
	 * @return
	 */
	protected static String getterName(String name){
		return methodName("get", name);
	}
	
	/**
	 * Creates a setter name from a field name.
	 * @param name
	 * @return
	 */
	protected static String setterName(String name){
		return methodName("set", name);
	}
	
	/**
	 * Build both the getter and setter name
	 * @param prefix
	 * @param name
	 * @return
	 */
	protected static String methodName(String prefix, String name){
		StringBuilder builder = new StringBuilder();
		builder.append(prefix);
		builder.append(name.substring(0, 1).toUpperCase());
		builder.append(name.substring(1, name.length()));
		return builder.toString();
	}
	
}
