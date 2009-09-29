package com.infosys.setlabs.dao;

/**
 * Wraps exceptions of particular data sources.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>"
 */
public class DataAccessException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Wraps data source exception.
	 * 
	 * @param e
	 */
	public DataAccessException(Exception e) {
		super(e.getMessage());
	}
}
