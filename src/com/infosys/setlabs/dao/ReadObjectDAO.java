package com.infosys.setlabs.dao;

import java.util.Collection;

/**
 * Interface for DAOs reading objects.
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 * @param <Type>
 *            Class of a domain object.
 */
public interface ReadObjectDAO<Type> {
	/**
	 * Finds an existing persistent object by its ID.
	 * 
	 * @param id
	 *            ID to find
	 * @return object
	 */
	public Type find(int id) throws DataAccessException;

	/**
	 * Finds all existing persistent objects.
	 * 
	 * @return object
	 */
	public Collection<Type> findAll() throws DataAccessException;
}
