package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import com.infosys.setlabs.dao.DAOSession;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAOSession;
import com.infosys.setlabs.miner.common.Configuration;
import com.infosys.setlabs.miner.dao.BasketFormatDAO;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.dao.MinerModuleDAO;
import com.infosys.setlabs.miner.dao.RepositoryFileDAO;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * MySQL DAO Factory
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlDAOFactory extends DAOFactory {
	// Database connection information
	private String server, user, password, database;
	private int port;

	// Properties
	private Properties prop;

	/**
	 * Creates a new MySQL DAO Factory
	 */
	public MysqlDAOFactory() {
		// Load the properties
		prop = Configuration.load("db");

		// Initialize connection parameters
		server = prop.getProperty("mysql.server");
		port = Integer.parseInt(prop.getProperty("mysql.port"));
		database = prop.getProperty("mysql.database");
		user = prop.getProperty("mysql.user");
		password = prop.getProperty("mysql.password");
	}

	@Override
	public void setConnectionArgs(HashMap<String, String> connectionArgs) {
		if (connectionArgs.get("server") != null)
			server = connectionArgs.get("server");
		if (connectionArgs.get("port") != null)
			port = Integer.parseInt(connectionArgs.get("port"));
		if (connectionArgs.get("database") != null)
			database = connectionArgs.get("database");
		if (connectionArgs.get("user") != null)
			user = connectionArgs.get("user");
		if (connectionArgs.get("password") != null)
			password = connectionArgs.get("password");
	}

	private Connection getConnection() throws DataAccessException {
		try {
			// Use MySQL directly for the moment, this will be replaced by a DAO
			MysqlDataSource mds = new MysqlDataSource();

			mds.setServerName(server);
			mds.setPortNumber(port);
			mds.setDatabaseName(database);

			// Cast into a data source
			DataSource ds = (DataSource) mds;

			return ds.getConnection(user, password);
		} catch (SQLException se) {
			throw new DataAccessException(se);
		}
	}

	@Override
	public DAOSession getSession() throws DataAccessException {
		return new JdbcDAOSession(this.getConnection());
	}

	@Override
	public RepositoryFileDAO getFileDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlRepositoryFileDAO(((JdbcDAOSession) session)
				.getConnection());
	}

	@Override
	public BasketFormatDAO getBasketFormatDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlBasketFormatDAO(((JdbcDAOSession) session)
				.getConnection());
	}

	@Override
	public MinerModuleDAO getMinerModuleDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlMinerModuleDAO(((JdbcDAOSession) session)
				.getConnection());
	}

	@Override
	public MinerFileDAO getMinerFileDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlMinerFileDAO(((JdbcDAOSession) session).getConnection());
	}

	@Override
	public FrequentItemSetDAO getFrequentItemSetDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlFrequentItemSetDAO(((JdbcDAOSession) session)
				.getConnection());
	}
}