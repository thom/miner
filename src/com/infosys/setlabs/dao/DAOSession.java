package com.infosys.setlabs.dao;

/**
 * Represents session on data source. Sessions span over longer period of time
 * and may include more smaller units of work. Smaller units of work are
 * performed in transactions. Exactly one active transaction can be associated
 * to the session. Instances of the classes satisfying this interface could be
 * created in data access object factory implementations. After the usage
 * sessions should be closed in order to release resources.
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public interface DAOSession {
	/**
	 * Returns current transaction of the session.
	 * 
	 * @return transaction object
	 */
	public DAOTransaction getTransaction();

	/**
	 * Closes session. Should be called to release resources.
	 */
	public void close() throws DataAccessException;
}