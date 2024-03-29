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
import com.infosys.setlabs.miner.dao.CommitDAO;
import com.infosys.setlabs.miner.dao.CommitMetricsDAO;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.infosys.setlabs.miner.dao.FrequentItemSetDAO;
import com.infosys.setlabs.miner.dao.FrequentItemSetMetricsDAO;
import com.infosys.setlabs.miner.dao.MinerFileDAO;
import com.infosys.setlabs.miner.dao.MinerFileMovesDAO;
import com.infosys.setlabs.miner.dao.MinerInfoDAO;
import com.infosys.setlabs.miner.dao.ModuleDAO;
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
	public BasketFormatDAO getBasketFormatDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlBasketFormatDAO(((JdbcDAOSession) session)
				.getConnection());
	}

	@Override
	public ModuleDAO getMinerModuleDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlModuleDAO(((JdbcDAOSession) session).getConnection());
	}

	@Override
	public MinerFileDAO getMinerFileDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlMinerFileDAO(((JdbcDAOSession) session).getConnection());
	}

	@Override
	public RepositoryFileDAO getRepositoryFileDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlRepositoryFileDAO(((JdbcDAOSession) session)
				.getConnection());
	}

	@Override
	public FrequentItemSetDAO getFrequentItemSetDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlFrequentItemSetDAO(((JdbcDAOSession) session)
				.getConnection());
	}

	@Override
	public MinerInfoDAO getMinerInfoDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlMinerInfoDAO(((JdbcDAOSession) session).getConnection());
	}

	@Override
	public FrequentItemSetMetricsDAO getFrequentItemSetMetricsDAO(
			DAOSession session) throws DataAccessException {
		return new MysqlFrequentItemSetMetricsDAO(((JdbcDAOSession) session)
				.getConnection());
	}

	@Override
	public MysqlGitupDAO getGitupDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlGitupDAO(((JdbcDAOSession) session).getConnection());
	}

	@Override
	public CommitDAO getCommitDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlCommitDAO(((JdbcDAOSession) session).getConnection());
	}

	@Override
	public CommitMetricsDAO getCommitMetricsDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlCommitMetricsDAO(((JdbcDAOSession) session)
				.getConnection());
	}

	@Override
	public MinerFileMovesDAO getMinerFileMovesDAO(DAOSession session)
			throws DataAccessException {
		return new MysqlMinerFileMovesDAO(((JdbcDAOSession) session)
				.getConnection());
	}
}