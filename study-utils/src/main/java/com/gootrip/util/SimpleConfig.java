/**
 * 
 */
package com.gootrip.util;

import java.net.URL;
import java.util.Iterator;

import nl.chess.it.util.config.Config;
import nl.chess.it.util.config.ConfigValidationResult;

/**
 * @author advance
 *
 */
public class SimpleConfig extends Config {
    /**
     * Name of the file we are looking for to read the configuration.
     */
    public static final String RESOURCE_NAME = "simpleconfig.properties";

    /**
     * Creates a new SimpleConfig object.
     */
    public SimpleConfig(String resourceName) {
        super(resourceName);
    }

    public boolean isThisANiceClass() {
        return getBoolean("doIlikeThisClass");
    }

    public String getWelcomeText() {
        return getString("property.welcome");
    }

    public URL getNiceWebsite() {
        return getURL("property.nicewebsite");
    }

    public int getNumberOfDays() {
        return getInt("property.numberofdays");
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Config config = new SimpleConfig(SimpleConfig.RESOURCE_NAME);

		ConfigValidationResult configResult = config.validateConfiguration();

		if (configResult.thereAreErrors()) {
		  System.out.println("Errors in configuration");

		  for (Iterator iter = configResult.getErrors().iterator(); iter.hasNext();) {
		    System.out.println(" > " + iter.next());
		  }

		  System.exit(1);
		}

		if (configResult.thereAreUnusedProperties()) {
		  System.out.println("Unused properties");

		  for (Iterator iter = configResult.getUnusedProperties().iterator(); iter.hasNext();) {
		    System.out.println(" > " + iter.next());
		  }
		}
	}
    
}
