package com.infosys.setlabs.dao;

/**
 * Represents the base interface for all data access interfaces. Data access
 * interfaces encapsulate access to data sources.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 * @param <Type>
 *            Class of a domain object.
 */
public interface ObjectDAO<Type> {
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
	public Type findAll() throws DataAccessException;		
	
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