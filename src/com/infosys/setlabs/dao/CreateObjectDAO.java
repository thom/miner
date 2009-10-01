package com.infosys.setlabs.dao;

public interface CreateObjectDAO<Type> {
	/**
	 * Creates new persistent object.
	 * 
	 * @param object
	 * @throws DataAccessException
	 */
	public void create(Type object) throws DataAccessException;
}
