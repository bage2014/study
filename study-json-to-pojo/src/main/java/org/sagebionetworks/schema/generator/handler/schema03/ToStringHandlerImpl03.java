package org.sagebionetworks.schema.generator.handler.schema03;

import java.util.ArrayList;
import java.util.Map;

import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.TYPE;
import org.sagebionetworks.schema.generator.handler.ToStringHandler;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

/**
 * This handler is responsible for adding toString() to a POJO.
 * @author ntiedema
 *
 */
public class ToStringHandlerImpl03 implements ToStringHandler {
	
	/**
	 * Add toString() to the POJO
	 * @param classSchema
	 * @param classType
	 */
	@Override
	public void addToStringMethod(ObjectSchema classSchema, JDefinedClass classType) {
		// There is nothing to do for interfaces.
		if(TYPE.INTERFACE == classSchema.getType()){
			throw new IllegalArgumentException("Cannot add hash and equals to an interface");
		}
		// Add the toString method
		addToString(classSchema, classType);
	}
	
	/**
	 * @param classSchema
	 * @param classType
	 * @return
	 */
	protected JMethod addToString(ObjectSchema classSchema, JDefinedClass classType){
		//create the method toString declaration
		//first parameter will make it a public method
		//second parameter is the return value
		//third parameter is the name of the method
		JMethod method = classType.method(JMod.PUBLIC, classType.owner().ref(String.class), "toString");
		
		//add the @Override annotation
		method.annotate(Override.class);
		
		//add the java docs
		JDocComment docs = method.javadoc();
		docs.add("Adds toString method to pojo.");
		docs.add("\n");
		docs.add("returns a string");
		docs.addReturn();
		
		//body will be the method's body
		JBlock body = method.body();
		
		// If we have a super then we use that to initialize the result
		//result is the string/param we will return
		JExpression resultInit = null;
		if(classSchema.getExtends() != null){
			resultInit = JExpr._super().invoke("toString");
		}else{
			// With no super() result starts as empty string
			resultInit = JExpr.lit("");
		}
		
		//declare the StringBuilder
		JVar result = body.decl(classType.owner().ref(StringBuilder.class), "result");
		JExpression newStBuilder = JExpr._new(classType.owner().ref(StringBuilder.class));
		body.assign(result, newStBuilder);
		
		//assign the resultInit
		body.add(result.invoke("append").arg(resultInit));
		
		//add the name of the class
		String nameOfClass = classType.fullName();
		body.add(result.invoke("append").arg(nameOfClass));
		body.add(result.invoke("append").arg(" ["));	
		
		// Now process each property
		Map<String, ObjectSchema> fieldMap = classSchema.getObjectFieldMap();
		for (String keyName : fieldMap.keySet()){
			ObjectSchema nextProp = fieldMap.get(keyName);
			
			//all properties in the schema should be represented in the class as fields
			JFieldVar field = classType.fields().get(keyName);
			if (field == null)
				throw new IllegalArgumentException(
						"Failed to find the JFieldVar for property: '"
								+ keyName + "' on class: " + classType.name());
			
			//check type for each property/field
			if (nextProp.getType() == null)
				throw new IllegalArgumentException("Property: '" + nextProp
						+ "' has a null TYPE on class: " + classType.name());
			TYPE type = nextProp.getType();
			
			if(TYPE.NUMBER == type || 
					TYPE.INTEGER == type || 
					TYPE.BOOLEAN == type || 
					TYPE.ARRAY == type || 
					TYPE.MAP == type || 
					TYPE.OBJECT == type ||
					TYPE.STRING == type ||
					TYPE.INTERFACE == type){
				//add an assignment statements to the body
				body.add(result.invoke("append").arg(keyName + "="));
				body.add(result.invoke("append").arg(field));
				body.add(result.invoke("append").arg(" "));
			}else {
				throw new IllegalArgumentException(keyName + 
						"has an invalid property type in it's schema " + nextProp);
			}
		}		
		//add closing ]
		body.add(result.invoke("append").arg("]"));
		
		body._return(result.invoke("toString"));
		return method;
	}
}


