package com.infosys.setlabs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Very basic database connection management.
 */
public class ConnectionManager {

	/**
	 * Get a database connection
	 * 
	 * @param vendor
	 * @param host
	 * @param database
	 * @param user
	 * @param password
	 * @return connection
	 * @throws SQLException
	 */
	public static Connection getConnection(String vendor, String host,
			String database, String user, String password) throws SQLException {
		String url = "jdbc:" + vendor + "://" + host + "/" + database;
		return DriverManager.getConnection(url, user, password);
	}
}
