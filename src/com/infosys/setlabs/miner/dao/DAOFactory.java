package com.infosys.setlabs.miner.dao;

import com.infosys.setlabs.dao.DAOSession;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.dao.mysql.MysqlDAOFactory;

/**
 * Represents abstract factory for creation of data access objects, which wrap
 * access to data source. The factory additionally provides access to session
 * objects.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 * @see com.infosys.setlabs.dao.DAOSession
 */
public abstract class DAOFactory {
	/**
	 * Database engine enumeration
	 */
	public static enum DatabaseEngine {
		MYSQL
	}

	/**
	 * Creates data access object factory that corresponds to the given data
	 * source.
	 * 
	 * @param factoryNo
	 *            of data source of the factory
	 * @return factory for data access objects
	 * @throws DataAccessException
	 */
	public static DAOFactory getDAOFactory(DatabaseEngine dbe) throws DataAccessException {
		switch (dbe) {
			case MYSQL :
				return new MysqlDAOFactory();
			default :
				return null;
		}
	}

	/**
	 * Abstract method for creation of the session object for access to data
	 * source. Sub-classes should return concrete session objects.
	 * 
	 * @return session
	 * @throws DataAccessException
	 * @see com.infosys.setlabs.dao.DAOSession
	 */
	public abstract DAOSession getSession() throws DataAccessException;

	/**
	 * Abstract method for file data access object.
	 * 
	 * @return session
	 * @throws DataAccessException
	 * @see dao.FileDAO
	 */
	public abstract int getFileDAO(int foo) throws DataAccessException;
	// public abstract FileDAO getFileDAO(DAOSession session)
	// throws DataAccessException;
}