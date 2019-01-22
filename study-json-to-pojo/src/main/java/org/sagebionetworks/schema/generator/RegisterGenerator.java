package org.sagebionetworks.schema.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.TYPE;
import org.sagebionetworks.schema.adapter.JSONEntity;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCase;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldRef;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JSwitch;
import com.sun.codemodel.JType;
import com.sun.codemodel.JVar;

/**
 * Auto-generates a register enumeration class.
 * 
 * @author John
 *
 */
public class RegisterGenerator {
	
	public static final String AUTO_GENERATED_MESSAGE = "Note: This class was auto-generated, and should not be directly modified.";
	
	/**
	 * Create a register for each including each class in the schema list.
	 * @param codeModel
	 * @param list
	 * @param registerClass
	 * @throws JClassAlreadyExistsException 
	 */
	public static JDefinedClass createRegister(JCodeModel codeModel, List<ObjectSchema> list, JDefinedClass regClass, String interfaceFullName) {
	
		// Only concrete class are added
		List<ObjectSchema> classList = new ArrayList<ObjectSchema>();
		for(ObjectSchema schema: list){
			if(isConcreteClass(schema)){
				classList.add(schema);
			}
		}
		// This field is the map
		JFieldRef mapRef = createMapFieldRef(codeModel, regClass);
		// Create the constructor.  This will build up the map.
		createConstructor(codeModel, classList, regClass, mapRef);
		
		// the new instance method
		createNewInstanceMethod(codeModel, classList, regClass, mapRef, interfaceFullName);

		createKeySetIterator(codeModel, regClass, mapRef);
		
		// Create the singleton
		createSingleton(codeModel, regClass);
		
		// Add the auto-generated title
		// Add the comments to the class
		JDocComment docs = regClass.javadoc();
		docs.add(AUTO_GENERATED_MESSAGE);
		docs.add("\n");
		docs.add("\nThis class provides an alternative to reflection which is not allowed in GWT clients.");
		docs.add("\nThe newInstance(String) method can be used to create new instances of any auto-generated");
		docs.add("\nconcrete class.");
		return regClass;
	}

	/**
	 * @param codeModel
	 * @param registerClass
	 * @return
	 */
	public static JDefinedClass createClassFromFullName(JCodeModel codeModel,
			String registerClass) {
		JDefinedClass regClass = null;
		try {
			regClass = codeModel._class(registerClass);
		} catch (JClassAlreadyExistsException e) {
			regClass = e.getExistingClass();
		}
		return regClass;
	}

	/**
	 * Create the keyset iterator.
	 * @param codeModel
	 * @param regClass
	 * @param mapRef
	 * @return
	 */
	protected static JMethod createKeySetIterator(JCodeModel codeModel, JDefinedClass regClass, JFieldRef mapRef) {
		JClass itType = codeModel.ref(Iterator.class).narrow(String.class);
		JMethod method = regClass.method(JMod.PUBLIC, itType, "getKeySetIterator");
		method.body()._return(mapRef.invoke("keySet").invoke("iterator"));
		JDocComment docs = method.javadoc();
		docs.add("Get the key set iterator.");
		docs.addReturn();
		return method;
	}
	
	/**
	 * Create the new instance method.
	 * @param codeModel
	 * @param regClass
	 * @param mapRef
	 * @param list
	 * @return
	 */
	protected static JMethod createNewInstanceMethod(JCodeModel codeModel, List<ObjectSchema> list, JDefinedClass regClass, JFieldRef mapRef, String interfaceFullName){
		// Create the new instance method
		JType returnType = null;
		if(interfaceFullName != null){
			returnType = getInterfaceClass(codeModel, interfaceFullName);
		}else{
			returnType = codeModel.ref(JSONEntity.class);
		}
	
		JMethod method = regClass.method(JMod.PUBLIC, returnType, "newInstance");
		JVar parm = method.param(codeModel.ref(String.class), "className");
		JBlock body = method.body();
		// First lookup the index for the class
		JInvocation callMapGet = mapRef.invoke("get");
		callMapGet.arg(parm);
		JVar intObject = body.decl(codeModel.ref(Integer.class), "intObject", callMapGet);
		JInvocation newIllegal = JExpr._new(codeModel.ref(IllegalArgumentException.class));
		newIllegal.arg(JExpr.lit("Cannot create new instance. Unknown class: ").plus(parm));
		// If the index is null then throw an IllegalArgumentException 
		body._if(intObject.eq(JExpr._null()))._then()._throw(newIllegal);
		// Create the index
		Integer g = new Integer(12);
		JVar index =body.decl(codeModel.INT, "index", intObject.invoke("intValue"));
		JSwitch jSwitch = body._switch(index);
		for(int i=0; i<list.size(); i++){
			ObjectSchema schema = list.get(i);
			JCase jCase = jSwitch._case(JExpr.lit(i));
			JDefinedClass classToRegister = getJDefinedClassForSchema(codeModel, schema);
			jCase.body()._return(JExpr._new(classToRegister));
		}
		JInvocation newIllegalState = JExpr._new(codeModel.ref(IllegalStateException.class));
		newIllegalState.arg(JExpr.lit("No match found for index: ").plus(index).plus(JExpr.lit(" for class name: ")).plus(parm));
		// This should not occur
		jSwitch._default().body()._throw(newIllegalState);
		
		JDocComment comment = method.javadoc();
		comment.add("Create a new instance of an auto-generated concrete class using the full class name.");
		comment.addParam(parm).add("The full class name of the class to get a new instance of.");
		comment.addReturn().add("New instance of the given class.");
		comment.addThrows(IllegalArgumentException.class).add("For unknown class names");

		
		return method;
	}
	
	private static JType getInterfaceClass(JCodeModel codeModel, String fullName){
		try {
			return codeModel._class(fullName);
		} catch (JClassAlreadyExistsException e) {
			return e.getExistingClass();
		}
	}

	/**
	 * Create the map field and return the references this.map
	 * @param codeModel
	 * @param regClass
	 * @return
	 */
	protected static JFieldRef createMapFieldRef(JCodeModel codeModel,JDefinedClass regClass) {
		JClass fieldType = codeModel.ref(Map.class).narrow(codeModel.ref(String.class), codeModel.ref(Integer.class));
		JFieldVar mapField = regClass.field(JMod.PRIVATE, fieldType, "map");
		// get the reference to this.map
		JFieldRef mapRef = JExpr._this().ref(mapField);
		return mapRef;
	}
	
	/**
	 * Is this a schema of a concrete class?
	 * @param schema
	 * @return
	 */
	protected static boolean isConcreteClass(ObjectSchema schema){
		// First is it an object
		if(TYPE.OBJECT != schema.getType()) return false;
		if(schema.getEnum() != null) return false;
		if(schema.getId() == null) return false;
		return true;
	}

	/**
	 * Creates the constructor.  Will initialize the map and populate it with each schema class.
	 * @param codeModel
	 * @param list
	 * @param regClass
	 * @return
	 */
	protected static JMethod createConstructor(JCodeModel codeModel, List<ObjectSchema> list, JDefinedClass regClass, JFieldRef mapRef) {
		// Create the Constructor
		JMethod constructor = regClass.constructor(JMod.PUBLIC);
		JBlock conBody = constructor.body();
		// This is the hash map
		JClass hashMap = codeModel.ref(HashMap.class).narrow(codeModel.ref(String.class), codeModel.ref(Integer.class));

		conBody.assign(mapRef, JExpr._new(hashMap));
		// Populate it with the values
		for(int i=0; i<list.size(); i++){
			ObjectSchema schema = list.get(i);
			// Add each entry
			JDefinedClass classToRegister = getJDefinedClassForSchema(codeModel, schema);
			JFieldRef classDotClass = classToRegister.staticRef("class");
			
			JInvocation putInvoke = mapRef.invoke("put");
			// First arg
			putInvoke.arg(classDotClass.invoke("getName"));
			// second arg
			putInvoke.arg(JExpr.lit(i));
			conBody.add(putInvoke);
		}
		return constructor;
	}
	
	/**
	 * Creates the singleton.
	 * @param codeModel
	 * @param list
	 * @param regClass
	 * @return
	 */
	protected static void createSingleton(JCodeModel codeModel, JDefinedClass regClass) {
		// Create the Constructor
		JFieldVar singletonField = regClass.field(JMod.PRIVATE | JMod.STATIC | JMod.FINAL, regClass, "SINGLETON", JExpr._new(regClass));

		JMethod method = regClass.method(JMod.PUBLIC | JMod.STATIC, regClass, "singleton");
		method.javadoc().add("The singleton of this register.");
		method.body()._return(singletonField);

	}

	public static JDefinedClass getJDefinedClassForSchema(JCodeModel codeModel,
			ObjectSchema schema) {
		if(schema.getId() == null) throw new IllegalArgumentException("Id cannot be null for an auto-generated Register");
		JDefinedClass classToRegister = null;
		try {
			classToRegister = codeModel._class(schema.getId());
		} catch (JClassAlreadyExistsException e) {
			classToRegister = e.getExistingClass();
		}
		return classToRegister;
	}

	/**
	 * 
	 * @param codeModel
	 * @param regClass
	 */
	public static JMethod createClassForName(JCodeModel codeModel, JDefinedClass regClass, JFieldRef mapRef) {
		JMethod getRegMethod = regClass.method(JMod.PUBLIC, codeModel.ref(Class.class), "forName");
		JVar param = getRegMethod.param(String.class, "className");
		getRegMethod.javadoc().add("Lookup a class using its full package name.  This works like Class.forName(className), but is GWT compatible.");
		JBlock body = getRegMethod.body();
		JInvocation getInvoke = mapRef.invoke("get");
		getInvoke.arg(param);
		body._return(getInvoke);
		return getRegMethod;
	}

	/**
	 * Extract the class name;
	 * @param registerClass
	 * @return
	 */
	protected static String getClassName(String fullClassName) {
		return fullClassName.substring(fullClassName.lastIndexOf('.')+1, fullClassName.length());
	}

	/**
	 * Get the package name from the fully qualified name.
	 * @param name
	 * @return
	 */
	protected static String getPackageName(String fullClassName){
		return fullClassName.substring(0, fullClassName.lastIndexOf('.'));
	}

}
