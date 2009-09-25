package com.infosys.setlabs.fism.common;

import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Loads configuration files.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>"
 */
public class Configuration {
	/**
	 * Loads a configuration file.
	 * 
	 * @param bundleName
	 *            Properties file in your classpath to load. The file must have
	 *            the ending "*.properties", but one must not specify it when
	 *            providing as a parameter, e.g. if your properties file is
	 *            named "foo.properties" then the parameter must be "foo".
	 * @return Loaded properties
	 */
	public static Properties load(String bundleName) {
		ResourceBundle rb = ResourceBundle.getBundle(bundleName);

		Properties result = new Properties();

		Enumeration<String> keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = keys.nextElement();
			result.put(key, rb.getString(key));
		}

		return result;
	}
}
