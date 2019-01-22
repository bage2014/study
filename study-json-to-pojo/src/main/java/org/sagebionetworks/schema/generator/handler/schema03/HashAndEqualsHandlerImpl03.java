package org.sagebionetworks.schema.generator.handler.schema03;

import java.util.Iterator;
import java.util.Map;

import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.TYPE;
import org.sagebionetworks.schema.generator.handler.HashAndEqualsHandler;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JConditional;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
import com.sun.codemodel.JVar;

public class HashAndEqualsHandlerImpl03 implements HashAndEqualsHandler {

	@Override
	public void addHashAndEquals(ObjectSchema classSchema,	JDefinedClass classType) {
		// There is nothing to do for interfaces.
		if(TYPE.INTERFACE == classSchema.getType()){
			throw new IllegalArgumentException("Cannot add hash and equals to an interface");
		}
		// Add the hash
		addHashCode(classSchema, classType);
		// Add equals
		addEquals(classSchema, classType);
	}
	
	/**
	 * Build the hashCode() method.
	 * @param classSchema
	 * @param classType
	 * @return
	 */
	protected JMethod addHashCode(ObjectSchema classSchema,	JDefinedClass classType){
		// First create the hashCode() method
		JMethod method = classType.method(JMod.PUBLIC, classType.owner().INT, "hashCode");
		method.annotate(Override.class);
		// Start on the body
		JBlock body = method.body();
		JVar prime = body.decl(JMod.FINAL, classType.owner().INT, "prime", JExpr.lit(31));
		// If we have a super then we use that to initialize the result
		JExpression resultInit = null;
		if(classSchema.getExtends() != null){
			resultInit = JExpr._super().invoke("hashCode");
		}else{
			// With no super() result starts as 1
			resultInit = JExpr.lit(1);
		}
		JVar result = body.decl(JMod.NONE, classType.owner().INT, "result", resultInit);
//		JVar addition = body.decl(JMod.NONE, classType.owner().INT, "addition", JExpr.lit(0));
		// This is used for doubles
		JVar temp = null;
		// Now add all fields 
		// Now process each property
		Map<String, ObjectSchema> fieldMap = classSchema.getObjectFieldMap();
		Iterator<String> keyIt = fieldMap.keySet().iterator();
		while (keyIt.hasNext()) {
			String propName = keyIt.next();
			ObjectSchema propSchema = fieldMap.get(propName);
			// Look up the field for this property
			JFieldVar field = classType.fields().get(propName);
			if (field == null)
				throw new IllegalArgumentException(
						"Failed to find the JFieldVar for property: '"
								+ propName + "' on class: " + classType.name());
			// Now process this field
			if (propSchema.getType() == null)
				throw new IllegalArgumentException("Property: '" + propSchema
						+ "' has a null TYPE on class: " + classType.name());
			TYPE type = propSchema.getType();

			// For each type we need to setup the add expression
			JExpression addExpression = null;
			// For all non-primitives we can use "hashCode"
			// If the object is not null then use hashCode() else, 0;
			addExpression = JOp.cond(field.eq(JExpr._null()), JExpr.lit(0),
					field.invoke("hashCode"));
//			if (TYPE.STRING == type || TYPE.ARRAY == type || TYPE.ANY == type
//					|| TYPE.NULL == type || TYPE.OBJECT == type) {
//				// If the object is not null then use hashCode() else, 0;
//				addExpression = JOp.cond(field.eq(JExpr._null()), JExpr.lit(0),
//						field.invoke("hashCode"));
//			} else if (TYPE.NUMBER == type) {
//				// Double requires special treatment
//				// Create or initialized the temp
//				JInvocation staticInvoke = classType.owner().ref(Double.class)
//						.staticInvoke("doubleToLongBits").arg(field);
//				if (temp == null) {
//					// Declare it the first time
//					temp = body.decl(JMod.NONE, classType.owner().LONG, "temp",
//							staticInvoke);
//				} else {
//					// Assign it all other times
//					body.assign(temp, staticInvoke);
//				}
//				addExpression = shiftXORCastLong(classType, temp);
//			} else if (TYPE.INTEGER == type) {
//				// Long requires special treatment
//				addExpression = shiftXORCastLong(classType, field);
//			} else if (TYPE.BOOLEAN == type) {
//				// boolean is also special
//				addExpression = JOp.cond(field, JExpr.lit(1231),
//						JExpr.lit(1237));
//			} else {
//				throw new IllegalArgumentException("Unknown type: " + type);
//			}
			// Put it all together
			body.assign(result, prime.mul(result).plus(addExpression));
		}

        body._return(result);
		return method;
	}
	
	/**
	 * Used to get the hash code for a long.
	 * @param classType
	 * @param longVar
	 * @return
	 */
	protected static JExpression shiftXORCastLong(JDefinedClass classType, JVar longVar){
		return JExpr.cast(classType.owner().INT, longVar.xor(longVar.shrz(JExpr.lit(32))));
	}
	
	/**
	 * Build the equals() method
	 * @param classSchema
	 * @param classType
	 * @return
	 */
	protected JMethod addEquals(ObjectSchema classSchema, JDefinedClass classType){
		// Create the equals() method
		JMethod method = classType.method(JMod.PUBLIC, classType.owner().BOOLEAN, "equals");
		method.annotate(Override.class);
		JVar obj = method.param(classType.owner().ref(Object.class), "obj");
		// Build up the body
		JBlock body = method.body();
		body._if(JOp.eq(JExpr._this(), obj))._then()._return(JExpr.lit(true));
		if(classSchema.getExtends() == null){
			// There is no super class so we just test for null
			body._if(JOp.eq(obj, JExpr._null()))._then()._return(JExpr.lit(false));
		}else{
			// ask the super class.
			body._if(JOp.not(JExpr._super().invoke("equals").arg(obj)))._then()._return(JExpr.lit(false));
		}

		body._if(JOp.ne(JExpr._this().invoke("getClass"), obj.invoke("getClass")))._then()._return(JExpr.lit(false));
		// declare the other
		JVar other = body.decl(JMod.NONE, classType, "other", JExpr.cast(classType, obj));
		
		// Now process each property
		Map<String, ObjectSchema> fieldMap = classSchema.getObjectFieldMap();
		Iterator<String> keyIt = fieldMap.keySet().iterator();
		while (keyIt.hasNext()) {
			String propName = keyIt.next();
			ObjectSchema propSchema = fieldMap.get(propName);
			// Look up the field for this property
			JFieldVar field = classType.fields().get(propName);
			if (field == null)
				throw new IllegalArgumentException(
						"Failed to find the JFieldVar for property: '"
								+ propName + "' on class: " + classType.name());
			// Now process this field
			if (propSchema.getType() == null)
				throw new IllegalArgumentException("Property: '" + propSchema
						+ "' has a null TYPE on class: " + classType.name());
			TYPE type = propSchema.getType();

			// For all non-primitives we can use "hashCode"
			// just use equals() for all objects
			JConditional outerCon = body._if(JOp.eq(field, JExpr._null()));
			outerCon._then()
					._if(JOp.ne(JExpr.ref(other, field), JExpr._null()))
					._then()._return(JExpr.lit(false));
			outerCon._elseif(
					JOp.not(field.invoke("equals").arg(
							JExpr.ref(other, field))))._then()
					._return(JExpr.lit(false));
//			if (TYPE.STRING == type || TYPE.ARRAY == type || TYPE.ANY == type
//					|| TYPE.NULL == type || TYPE.OBJECT == type) {
//				// just use equals() for all objects
//				JConditional outerCon = body._if(JOp.eq(field, JExpr._null()));
//				outerCon._then()
//						._if(JOp.ne(JExpr.ref(other, field), JExpr._null()))
//						._then()._return(JExpr.lit(false));
//				outerCon._elseif(
//						JOp.not(field.invoke("equals").arg(
//								JExpr.ref(other, field))))._then()
//						._return(JExpr.lit(false));
//			} else if (TYPE.NUMBER == type) {
//				// doubles are special
//				JClass doubleClass = classType.owner().ref(Double.class);
//				body._if(
//						JOp.ne(doubleClass.staticInvoke("doubleToLongBits")
//								.arg(field),
//								doubleClass.staticInvoke("doubleToLongBits")
//										.arg(JExpr.ref(other, field))))._then()
//						._return(JExpr.lit(false));
//			} else if (TYPE.INTEGER == type || TYPE.BOOLEAN == type) {
//				// primitives are easy
//				body._if(JOp.ne(field, JExpr.ref(other, field)))._then()
//						._return(JExpr.lit(false));
//			} else {
//				throw new IllegalArgumentException("Unknown type: " + type);
//			}
		}

        // Add the last return true
        body._return(JExpr.lit(true));
		return method;
	}

}
