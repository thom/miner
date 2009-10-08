package com.infosys.setlabs.dao;

/**
 * Interface for DAOs deleting objects.
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 * @param <Type>
 *            Class of a domain object.
 */
public interface DeleteObjectDAO<Type> {
	/**
	 * Deletes existing persistent object.
	 * 
	 * @param object
	 *            object to delete
	 * @throws DataAccessException
	 */
	public void delete(Type object) throws DataAccessException;
}
