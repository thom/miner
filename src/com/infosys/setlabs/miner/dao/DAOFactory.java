package com.infosys.setlabs.miner.dao;

import java.util.HashMap;

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
	public static DAOFactory getDAOFactory(DatabaseEngine dbe)
			throws DataAccessException {
		switch (dbe) {
			case MYSQL :
				return new MysqlDAOFactory();
			default :
				return null;
		}
	}

	/**
	 * Set connection arguments
	 * 
	 * @param args
	 */
	public abstract void setConnectionArgs(
			HashMap<String, String> connectionArgs);

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
	 * @param session
	 * @return RepositoryFileDAO
	 * @throws DataAccessException
	 */
	public abstract RepositoryFileDAO getFileDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for basket format data access object
	 * 
	 * @param session
	 * @return BasketFormatDAO
	 * @throws DataAccessException
	 */
	public abstract BasketFormatDAO getBasketFormatDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for shiatsu (data massaging) data access object
	 * 
	 * @param session
	 * @return ShiatsuDAO
	 * @throws DataAccessException
	 */
	public abstract ShiatsuDAO getShiatsuDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for miner module data access object
	 * 
	 * @param session
	 * @return MinerModuleDAO
	 * @throws DataAccessException
	 */
	public abstract MinerModuleDAO getMinerModuleDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for miner file data access object
	 * 
	 * @param session
	 * @return MinerFileDAO
	 * @throws DataAccessException
	 */
	public abstract MinerFileDAO getMinerFileDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for miner frequent item set data access object
	 * 
	 * @param session
	 * @return FrequentItemSetDAO
	 * @throws DataAccessException
	 */
	public abstract FrequentItemSetDAO getMinerFrequentItemSetDAO(DAOSession session)
			throws DataAccessException;
}