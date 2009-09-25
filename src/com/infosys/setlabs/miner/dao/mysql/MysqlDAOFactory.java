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

	private Connection getConnection() throws DataAccessException {
		try {
			// Load the properties
			Properties prop = Configuration.load("db");

			// Use MySQL directly for the moment, this will be replaced by a DAO
			MysqlDataSource mds = new MysqlDataSource();

			// Initialize connection parameters
			if (database == null)
				database = prop.getProperty("mysql.database");
			if (user == null)
				database = prop.getProperty("mysql.user");
			if (password == null)
				password = prop.getProperty("mysql.password");
			
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

	@Override
	public DAOSession getSession() throws DataAccessException {
		return new JdbcDAOSession(this.getConnection());
	}

	@Override
	public int getFileDAO(int foo) throws DataAccessException {
		return 23;
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
}