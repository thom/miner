package com.infosys.setlabs.dao;

public interface DeleteObjectDAO<Type> {	
	/**
	 * Deletes existing persistent object.
	 * 
	 * @param object
	 * @throws DataAccessException
	 */
	public void delete(Type object) throws DataAccessException;	
}
