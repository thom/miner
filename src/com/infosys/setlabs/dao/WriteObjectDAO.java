package com.infosys.setlabs.dao;

public interface WriteObjectDAO<Type> {
	/**
	 * Creates new persistent object.
	 * 
	 * @param object
	 * @throws DataAccessException
	 */
	public void create(Type object) throws DataAccessException;

	/**
	 * Updates existing persistent object.
	 * 
	 * @param object
	 * @throws DataAccessException
	 */
	public void update(Type object) throws DataAccessException;
	
	/**
	 * Deletes existing persistent object.
	 * 
	 * @param object
	 * @throws DataAccessException
	 */
	public void delete(Type object) throws DataAccessException;	
}
