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
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public abstract class DAOFactory {
	/**
	 * Database engines
	 */
	public static enum DatabaseEngine {
		MYSQL
	}

	/**
	 * Creates data access object factory that corresponds to the given data
	 * source.
	 * 
	 * @param dbe
	 *            database engine to use
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
	 * Sets connection arguments
	 * 
	 * @param args
	 *            arguments
	 */
	public abstract void setConnectionArgs(
			HashMap<String, String> connectionArgs);

	/**
	 * Abstract method for creation of the session object for access to data
	 * source. Sub-classes should return concrete session objects.
	 * 
	 * @return session
	 * @throws DataAccessException
	 */
	public abstract DAOSession getSession() throws DataAccessException;

	/**
	 * Abstract method for file data access object.
	 * 
	 * @param session
	 *            session to connect to
	 * @return RepositoryFileDAO
	 * @throws DataAccessException
	 */
	public abstract RepositoryFileDAO getFileDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for basket format data access object
	 * 
	 * @param session
	 *            session to connect to
	 * @return BasketFormatDAO
	 * @throws DataAccessException
	 */
	public abstract BasketFormatDAO getBasketFormatDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for miner module data access object
	 * 
	 * @param session
	 *            session to connect to
	 * @return ModuleDAO
	 * @throws DataAccessException
	 */
	public abstract ModuleDAO getMinerModuleDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for miner file data access object
	 * 
	 * @param session
	 *            session to connect to
	 * @return MinerFileDAO
	 * @throws DataAccessException
	 */
	public abstract MinerFileDAO getMinerFileDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for miner frequent item set data access object
	 * 
	 * @param session
	 *            session to connect to
	 * @return FrequentItemSetDAO
	 * @throws DataAccessException
	 */
	public abstract FrequentItemSetDAO getFrequentItemSetDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for miner info data access object
	 * 
	 * @param session
	 *            session to connect to
	 * @return MinerInfoDAO
	 * @throws DataAccessException
	 */
	public abstract MinerInfoDAO getMinerInfoDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for frequent item set metrics data access object
	 * 
	 * @param session
	 * @return FrequentItemSetMetricsDAO
	 * @throws DataAccessException
	 */
	public abstract FrequentItemSetMetricsDAO getFrequentItemSetMetricsDAO(
			DAOSession session) throws DataAccessException;

	/**
	 * Abstract method for gitup data access object
	 * 
	 * @param session
	 * @return FrequentItemSetMetricsDAO
	 * @throws DataAccessException
	 */
	public abstract GitupDAO getGitupDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for commit data access object
	 * 
	 * @param session
	 * @return FrequentItemSetMetricsDAO
	 * @throws DataAccessException
	 */
	public abstract CommitDAO getCommitDAO(DAOSession session)
			throws DataAccessException;

	/**
	 * Abstract method for commit metrics data access object
	 * 
	 * @param session
	 * @return FrequentItemSetMetricsDAO
	 * @throws DataAccessException
	 */
	public abstract CommitMetricsDAO getCommitMetricsDAO(DAOSession session)
			throws DataAccessException;
}