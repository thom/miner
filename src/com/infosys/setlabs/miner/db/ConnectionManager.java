package com.infosys.setlabs.miner.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import com.infosys.setlabs.miner.common.Configuration;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Very basic database connection management.
 */
public class ConnectionManager {

	/**
	 * Get a database connection
	 * 
	 * @param database
	 * @param user
	 * @param password
	 * @return database connection
	 * @throws SQLException
	 */
	public static Connection getConnection(String database, String user,
			String password) throws SQLException {
		// Load the properties
		Properties prop = Configuration.load("db");

		// Use MySQL directly for the moment, this will be replaced by a DAO
		MysqlDataSource mds = new MysqlDataSource();
		mds.setDatabaseName(database);
		mds.setServerName(prop.getProperty("mysql.server"));
		mds.setPortNumber(Integer.parseInt(prop.getProperty("mysql.port")));

		// Cast into a data source
		DataSource ds = (DataSource) mds;

		return ds.getConnection(user, password);
	}
}
