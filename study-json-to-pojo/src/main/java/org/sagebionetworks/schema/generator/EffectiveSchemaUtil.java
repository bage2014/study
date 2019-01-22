package org.sagebionetworks.schema.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONObject;
import org.sagebionetworks.schema.ObjectSchema;
import org.sagebionetworks.schema.adapter.JSONEntity;
import org.sagebionetworks.schema.adapter.JSONObjectAdapter;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.adapter.org.json.JSONObjectAdapterImpl;

/**
 * For a schema that inherits properties from another schema (through "extends"
 * or "implements"), the "effective" schema collapses all properties from
 * inheritances or interfaces into first class properties. This is useful for a
 * client that just wants a simple representation of a schema, without the need
 * for walking class hierarchy, or interfaces.
 * 
 * @author jmhill
 *
 */
public class EffectiveSchemaUtil {

	private static final String FORWARD_SLASH = "/";
	private static final String ESCAPED_DOT = "\\.";
	private static final String DOT_JSON = "-effective.json";

	/**
	 * Generate the JSON of the "effective" schema.
	 * 
	 * @param schema
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static String generateJSONofEffectiveSchema(ObjectSchema schema) throws JSONObjectAdapterException {
		// First create the effective schema, then write it JSON.
		ObjectSchema effective = generateEffectiveSchema(schema);
		JSONObjectAdapter adapter = effective.writeToJSONObject(new JSONObjectAdapterImpl());
		return adapter.toJSONString();
	}

	/**
	 * Generate the "effective" schema object.
	 * 
	 * @param schema
	 * @return
	 * @throws JSONObjectAdapterException
	 */
	public static ObjectSchema generateEffectiveSchema(ObjectSchema schema) throws JSONObjectAdapterException {
		if (schema == null)
			throw new IllegalArgumentException("Schema cannot be null");
		// First make a copy of the schema
		JSONObjectAdapter adapter = schema.writeToJSONObject(new JSONObjectAdapterImpl());
		;
		adapter = new JSONObjectAdapterImpl(adapter.toJSONString());
		ObjectSchema copy = new ObjectSchema(adapter);
		// Clear the extends and and implements
		copy.setExtends(null);
		copy.setImplements(null);
		copy.setProperties((LinkedHashMap<String, ObjectSchema>) schema.getObjectFieldMap());
		ObjectSchema.recursivelyAddAllExtendsProperties(copy.getProperties(), schema);
		// Add any properties from the extends.
		return copy;
	}

	/**
	 * Write the effective schema file for the given schemas to the given output
	 * directory.
	 * 
	 * @param outputDir
	 * @param schemas
	 * @throws JSONObjectAdapterException 
	 * @throws IOException 
	 */
	public static void generateEffectiveSchemaFiles(File outputDir, List<ObjectSchema> schemas) throws JSONObjectAdapterException, IOException {
		for(ObjectSchema schema: schemas) {
			generateEffectiveSchemaFile(outputDir, schema);
		}
	}

	/**
	 * Write the effective schema file for the given schema to the given output
	 * directory.
	 * @param outputDir
	 * @param schema
	 * @throws JSONObjectAdapterException
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	public static File generateEffectiveSchemaFile(File outputDir, ObjectSchema schema)
			throws JSONObjectAdapterException, IOException, UnsupportedEncodingException {
		String schemaJson = generateJSONofEffectiveSchema(schema);
		// Convert the single line JSON to multi-line with indentation.
		JSONObject object = new JSONObject(schemaJson);
		schemaJson = object.toString(3);
		File resultFile = createFileForSchema(outputDir, schema);
		try (FileOutputStream fos = new FileOutputStream(resultFile)) {
			fos.write(schemaJson.getBytes("UTF-8"));
			fos.flush();
		}
		return resultFile;
	}
	
	/**
	 * Create the .json file name for the given schema.
	 * @param schema
	 * @return
	 */
	public static final File createFileForSchema(final File outputDir, final ObjectSchema schema) {
		if(schema == null) {
			throw new IllegalArgumentException("Schema cannot be null");
		}
		if(schema.getId() == null) {
			throw new IllegalArgumentException("Schema.id cannot be null");
		}
		// Parent directory starts as the output directory.
		File parent = outputDir;
		String[] packageNameSplit = schema.getId().split(ESCAPED_DOT);
		// add each folder
		for(int i=0; i<packageNameSplit.length-1; i++) {
			parent = new File(parent, packageNameSplit[i]);
		}
		// ensure the full parent path exists
		parent.mkdirs();
		// the name of the file.
		String name = packageNameSplit[packageNameSplit.length-1]+DOT_JSON;
		return new File(parent, name);
	}
	
	/**
	 * Load the effective schema from the classpath for the given JSONEntity.
	 * @param entity
	 * @return
	 * @throws IOException 
	 */
	public static String loadEffectiveSchemaFromClasspath(Class<? extends JSONEntity> clazz) throws IOException {
		if(clazz == null) {
			throw new IllegalArgumentException("JSONEntity cannot be null");
		}
		String fileName = clazz.getName().replaceAll(ESCAPED_DOT, FORWARD_SLASH)+DOT_JSON;
		try(InputStream stream = clazz.getClassLoader().getResourceAsStream(fileName)){
			if(stream == null) {
				throw new IllegalArgumentException("Unable to find :"+fileName+" on classpath");
			}
			return FileUtils.readToString(stream);
		}
	}

}
