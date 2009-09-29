package com.infosys.setlabs.dao;

import java.util.Collection;

public interface ReadObjectDAO<Type> {
	/**
	 * Finds an existing persistent object by its ID.
	 * 
	 * @param id
	 * @return object
	 */
	public Type find(int id) throws DataAccessException;
	
	/**
	 * Finds all existing persistent objects.
	 * 
	 * @param id
	 * @return object
	 */
	public Collection<Type> findAll() throws DataAccessException;	
}
