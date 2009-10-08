package com.infosys.setlabs.dao;

/**
 * Represents transaction on the data source. Short, atomic units of work should
 * be executed in transactions. Transaction is always related to the session,
 * which represents longer unit of work.
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface DAOTransaction {
	/**
	 * Marks the begining of the transaction.
	 * 
	 * @throws DataAccessException
	 */
	public void begin() throws DataAccessException;

	/**
	 * Commits changes made in the transaction.
	 * 
	 * @throws DataAccessException
	 */
	public void commit() throws DataAccessException;

	/**
	 * Rolls back all changes made in transaction.
	 * 
	 * @throws DataAccessException
	 */
	public void abort() throws DataAccessException;

	/**
	 * Returns session in which transaction is executing.
	 * 
	 * @return session
	 * @throws DataAccessException
	 */
	public DAOSession getSession() throws DataAccessException;
}