package org.sagebionetworks.schema.generator;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONException;
import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.TYPE;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.adapter.org.json.JSONObjectAdapterImpl;
import org.sagebionetworks.schema.generator.handler.HandlerFactory;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.writer.FileCodeWriter;
import com.sun.codemodel.writer.ProgressCodeWriter;

/**
 * Generates DTO type POJOs from JSON schema files.
 * 
 * @author jmhill
 *
 */
public class SchemaToPojo {
	
	/**
	 * Will read all schemas in the passed directory (or file) and create java files for each in the output directory.
	 * All classes will be created using the provided package name.
	 * @param schemaSource
	 * @param outputDir
	 * @param createRegister
	 * @param factory
	 * @param log
	 * @throws IOException
	 * @throws JSONObjectAdapterException
	 * @throws ClassNotFoundException
	 */
	public static void generatePojos(File schemaSource, File outputDir, String createRegister, HandlerFactory factory, StringBuilder log)
			throws IOException, JSONObjectAdapterException, ClassNotFoundException {
		if(schemaSource == null) throw new IllegalArgumentException("schemaSource cannot be null");
		if(outputDir == null) throw new IllegalArgumentException("outputDir cannot be null");
		if(factory == null) throw new IllegalArgumentException("The HandlerFactory cannot be null");
		// process each file
		Iterator<File> iterator = FileUtils.getRecursiveIterator(schemaSource, new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				// Only include .json files
				return pathname.getName().toLowerCase().endsWith(".json");
			}
		});
		
		// Build up the list of schemas from the files
		List<ObjectSchema> schemaList = new ArrayList<ObjectSchema>();
		while(iterator.hasNext()){
			File file = iterator.next();
			String string = FileUtils.readToString(file);
			// Create a new schema
			ObjectSchema schema;
			try {
				schema = new ObjectSchema(new JSONObjectAdapterImpl(string));
			} catch (JSONObjectAdapterException e) {
				if (e.getCause() instanceof JSONException) {
					JSONException e2 = (JSONException) e.getCause();
					throw new JSONObjectAdapterException(file.getAbsolutePath() + ": " + e2.getMessage(), e2);
				}
				throw e;
			}
			// Now if the schema does not have a name use the file name
			if(schema.getName() == null){
				schema.setName(extractSchemaNameFromFileName(file));
			}
			// Set the id
			String packageName = getPackageNameFromFiles(schemaSource, file);
			schema.setId(packageName+schema.getName());
			// Each base schema must be an object even if it is not set
			if(schema.getType() == null){
				schema.setType(TYPE.OBJECT);
			}
			// Add it to the list
			schemaList.add(schema);
		}
		// JCodeModel is used as the document model for the classes.
		JCodeModel codeModel = new JCodeModel();
		
		// Create the register class if it is provided
		JDefinedClass registerClass = null;
		if (createRegister != null) {
			registerClass = RegisterGenerator.createClassFromFullName(codeModel, createRegister);
		}
		
		// The drive does the recursive work and drives the handlers
		PojoGeneratorDriver driver = new PojoGeneratorDriver(factory);
		driver.createAllClasses(codeModel, schemaList);
		
		// When provided, create a register for all of the classes in the list.
		if(createRegister != null){
			RegisterGenerator.createRegister(codeModel, schemaList, registerClass, null);
		}
		
		// The final step is to generate the classes
		if(!outputDir.exists()){
			outputDir.mkdirs();
		}
		
		if(createRegister != null) {
			// create an effective schema file for each schema.
			EffectiveSchemaUtil.generateEffectiveSchemaFiles(outputDir, schemaList);
		}

		CodeWriter sources = new ChangeFileCodeWriter(outputDir, log);
		CodeWriter resources = new FileCodeWriter(outputDir);
		sources = new ProgressCodeWriter(sources, System.out);
		resources = new ProgressCodeWriter(resources, System.out);
		codeModel.build(sources, resources);
	}
	
	/**
	 * Extract the package name using the root file and the json file.
	 * @param rootDir
	 * @param jsonFile
	 * @return
	 */
	public static String getPackageNameFromFiles(File rootDir, File jsonFile){
		String rootPath = rootDir.getAbsolutePath();
		String filePath = jsonFile.getAbsolutePath();
		// If they are the same file then use the default package
		if(rootPath.equals(filePath)){
			return "";
		}
		int start = rootPath.length();
		int end = filePath.indexOf(jsonFile.getName());
		String sub = filePath.substring(start, end);
		sub = sub.replaceAll("\\\\", ".");
		sub = sub.replaceAll("/", ".");
		start = 0;
		end = sub.length();
		if(sub.startsWith(".")){
			start++;
		}
		return sub.substring(start, end);
	}
	
	/**
	 * Get the schema name from the file name
	 * @param file
	 * @return
	 */
	protected static String extractSchemaNameFromFileName(File file){
		String name = file.getName();
		return extractSchemaNameFromFileName(name);
	}

	/**
	 * Extract the schema name from a file name.
	 * @param name
	 * @return
	 */
	public static String extractSchemaNameFromFileName(String name) {
		name = name.substring(0, name.toLowerCase().indexOf(".json"));
		// Make sure the first character is upper case
		StringBuilder builder = new StringBuilder();
		builder.append(name.substring(0, 1).toUpperCase());
		builder.append(name.substring(1, name.length()));
		return builder.toString();
	}

}
