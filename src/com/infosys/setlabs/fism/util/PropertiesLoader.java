package com.infosys.setlabs.fism.util;

import java.util.Properties;

/**
 * Loads properties files.
 * 
 * @author "Thomas Weibel <thom@ikhono.net>"
 */

public abstract class PropertiesLoader {
	/**
	 * Loads a property file.
	 * 
	 * @param propertiesFile
	 *            properties file to load
	 * @return Loaded properties
	 */
	public static Properties load(String propertiesFile) {
		Properties properties = new Properties();

		try {
			properties.load(new java.io.FileInputStream(propertiesFile));
		} catch (Exception e) {
			System.out.println("Error loading properties '" + propertiesFile
					+ "'\n" + e);
		}

		return properties;
	}
}