package com.infosys.setlabs.dao;

/**
 * Interface for DAOs creating new objects.
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 * @param <Type>
 *            Class of a domain object.
 */
public interface CreateObjectDAO<Type> {
	/**
	 * Creates new persistent object.
	 * 
	 * @param object
	 *            object to create
	 * @throws DataAccessException
	 */
	public Type create(Type object) throws DataAccessException;
}
