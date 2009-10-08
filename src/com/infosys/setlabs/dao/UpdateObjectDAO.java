package com.infosys.setlabs.dao;

/**
 * Interface for DAOs updating objects.
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 * @param <Type>
 *            Class of a domain object.
 */
public interface UpdateObjectDAO<Type> {
	/**
	 * Updates existing persistent object.
	 * 
	 * @param object
	 *            object to update
	 * @throws DataAccessException
	 */
	public void update(Type object) throws DataAccessException;
}
