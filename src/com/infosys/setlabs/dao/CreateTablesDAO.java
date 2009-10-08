package com.infosys.setlabs.dao;

/**
 * Interface for DAOs creating new tables.
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface CreateTablesDAO {
	/**
	 * Creates new tables
	 * 
	 * @throws DataAccessException
	 */
	public void createTables() throws DataAccessException;
}
