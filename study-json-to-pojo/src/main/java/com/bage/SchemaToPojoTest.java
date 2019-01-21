package com.bage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.sagebionetworks.schema.adapter.JSONObjectAdapterException;
import org.sagebionetworks.schema.generator.FileUtils;
import org.sagebionetworks.schema.generator.SchemaToPojo;
import org.sagebionetworks.schema.generator.handler.HandlerFactory;
import org.sagebionetworks.schema.generator.handler.schema03.HandlerFactoryImpl03;

public class SchemaToPojoTest {

	File outputDir;

	@Test
	public void testExtractSchemaNameFromFileName() {
        String fileName = "someFileName.JSON";
		String result = SchemaToPojo.extractSchemaNameFromFileName(fileName);
		assertNotNull(result);
		assertEquals("SomeFileName", result);
	}

	@Before
	public void before() throws IOException {
		// Create a temp directory for output
		outputDir = FileUtils.createTempDirectory("output");
	}

	@After
	public void after() {
		// Delete the output directory
		
		System.out.println(outputDir.getAbsolutePath());
		// FileUtils.recursivelyDeleteDirectory(outputDir);
		// assertFalse(outputDir.exists());
	}

	@Test
	public void loadSingleFile() throws IOException,
			JSONObjectAdapterException, ClassNotFoundException {
		// Load form the sample file
		File sampleFile = new File("src/main/java/com/bage/ExampleSchema.json");
		assertTrue("Test file does not exist: " + sampleFile.getAbsolutePath(),
				sampleFile.exists());
		// Create the class
		HandlerFactory factory = new HandlerFactoryImpl03();
		// Generate the class
		SchemaToPojo.generatePojos(sampleFile, outputDir,"org.sample.Register", factory, new StringBuilder());
		// Make sure the file exists
		File result = new File(outputDir, "Product.java");
		System.out.println(result.getAbsolutePath());
		assertTrue(result.exists());
		String resultString1 = FileUtils.readToString(result);
		System.out.println(resultString1);
		
		return ;
		/*
		// Make sure the register class exists
		result = new File(outputDir, "org/sample/Register.java");
		System.out.println(result.getAbsolutePath());
		assertTrue(result.exists());
		
		// Load the file string
		String resultString = FileUtils.readToString(result);
		System.out.println(resultString);*/
	}

}
