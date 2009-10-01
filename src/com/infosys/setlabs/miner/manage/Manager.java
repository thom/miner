package com.infosys.setlabs.miner.manage;

import java.util.HashMap;

import com.infosys.setlabs.dao.DAOSession;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.miner.common.MinerException;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.dao.DAOFactory.DatabaseEngine;

/**
 * Represents base abstract class for all business manager classes of an
 * application.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 */

public abstract class Manager {
	/**
	 * Specifies the current database engine
	 */
	public static DatabaseEngine currentDatabaseEngine = DAOFactory.DatabaseEngine.MYSQL;

	/**
	 * Returns the current database engine
	 * 
	 * @return current database engine
	 */
	public static DatabaseEngine getCurrentDatabaseEngine() {
		return currentDatabaseEngine;
	}
	/**
	 * @param currentDatabaseEngine
	 *            The currentDatasource to set.
	 */
	public static void setCurrentDatabaseEngine(DatabaseEngine databaseEngine) {
		currentDatabaseEngine = databaseEngine;
	}

	protected DAOFactory factory;

	protected DAOSession session;

	/**
	 * Constructor initializes data source factory and session. Should be called
	 * from inherited classes.
	 * 
	 * @throws MinerException
	 */
	public Manager(HashMap<String, String> connectionArgs)
			throws MinerException {
		try {
			this.setFactory(DAOFactory.getDAOFactory(currentDatabaseEngine));
			this.getFactory().setConnectionArgs(connectionArgs);
			this.setSession(this.getFactory().getSession());
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	/**
	 * Closes the session associated to manager instance.
	 * 
	 * @throws MinerException
	 */
	public void close() throws MinerException {
		try {
			this.getSession().close();
		} catch (DataAccessException e) {
			throw new MinerException(e);
		}
	}

	/**
	 * Returns the factory.
	 * 
	 * @return factory
	 */
	public DAOFactory getFactory() {
		return this.factory;
	}

	/**
	 * Returns the session.
	 * 
	 * @return session
	 */
	public DAOSession getSession() {
		return this.session;
	}

	/**
	 * Sets the factory.
	 * 
	 * @param factory
	 *            factory to set
	 */
	public void setFactory(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Sets the session.
	 * 
	 * @param session
	 *            session to set
	 */
	public void setSession(DAOSession session) {
		this.session = session;
	}
}
