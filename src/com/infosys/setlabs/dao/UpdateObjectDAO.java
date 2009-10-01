package com.infosys.setlabs.dao;

public interface UpdateObjectDAO<Type> {
	/**
	 * Updates existing persistent object.
	 * 
	 * @param object
	 * @throws DataAccessException
	 */
	public void update(Type object) throws DataAccessException;
}
