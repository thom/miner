package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.infosys.setlabs.dao.DAOSession;
import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAOSession;
import com.infosys.setlabs.miner.common.Configuration;
import com.infosys.setlabs.miner.dao.DAOFactory;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * MySQL DAO Factory
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 */
public class MysqlDAOFactory extends DAOFactory {
	// Database connection information
	private String database, user, password;
	
	private Properties prop;
	
	public MysqlDAOFactory() {
		// Load the properties
		prop = Configuration.load("db");
		
		// Initialize connection parameters
		user = prop.getProperty("mysql.user");
		password = prop.getProperty("mysql.password");
		database = prop.getProperty("mysql.database");
	}

	public Connection getConnection() throws DataAccessException {
		try {
			// Use MySQL directly for the moment, this will be replaced by a DAO
			MysqlDataSource mds = new MysqlDataSource();

			mds.setDatabaseName(database);
			mds.setServerName(prop.getProperty("mysql.server"));
			mds.setPortNumber(Integer.parseInt(prop.getProperty("mysql.port")));

			// Cast into a data source
			DataSource ds = (DataSource) mds;

			return ds.getConnection(user, password);
		} catch (SQLException se) {
			throw new DataAccessException(se);
		}
	}

	/**
	 * Set the connection arguments with an array
	 * 
	 * @param args
	 *            Array of strings: args[0]: database, args[1]: user, args[2]:
	 *            password
	 */
	@Override
	public void setConnectionArgs(String[] args) {
		if (args[0] != null)
			this.database = args[0];
		if (args[1] != null)
			this.user = args[1];
		if (args[2] != null)
			this.password = args[2];
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public DAOSession getSession() throws DataAccessException {
		return new JdbcDAOSession(this.getConnection());
	}

	@Override
	public int getFileDAO(int foo) throws DataAccessException {
		return 23;
	}
}