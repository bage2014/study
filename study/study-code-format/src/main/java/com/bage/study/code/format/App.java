package com.bage.study.code.format;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws FormatterException
    {
    	String sourceString = "import com.google.googlejavaformat.java.Formatter;\r\n" + 
    			"public class App {public static void main( String[] args )\r\n" + 
    			"    {String sourceString = \"\";\r\n" + 
    			"		String formattedSource = new Formatter().formatSource(sourceString );\r\n" + 
    			"\r\n" + 
    			"        System.out.println( \"Hello World!\" );\r\n" + 
    			"    }\r\n" + 
    			"}";
    	System.out.println( sourceString );
    	
    	System.out.println("-------------------");
    	
		String formattedSource = new Formatter().formatSource(sourceString );

        
        System.out.println( formattedSource );

        
        
    }
    
    
}
