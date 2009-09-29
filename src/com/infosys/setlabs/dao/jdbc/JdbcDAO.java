package com.infosys.setlabs.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract JDBC Data Access Object.
 * 
 * @author "Thomas Weibel <thomas_401709@infosys.com>
 */
public abstract class JdbcDAO {
	// JDBC connection to the database
	private Connection connection;

	/**
	 * Create a new DAO.
	 * 
	 * @param conn
	 *            connection
	 */
	public JdbcDAO(Connection conn) {
		this.setConnection(conn);
	}

	/**
	 * Close a result set.
	 * 
	 * @param resultSet
	 *            result set to close
	 */
	protected void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
			}
		}
	}

	/**
	 * Close a prepared statement.
	 * 
	 * @param statement
	 *            prepared statement to close
	 */
	protected void closeStatement(PreparedStatement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
			}
		}
	}

	/**
	 * Get a connection.
	 * 
	 * @return connection
	 */
	protected Connection getConnection() {
		return this.connection;
	}

	/**
	 * Set the connection.
	 * 
	 * @param conn
	 *            connection to set
	 */
	protected void setConnection(Connection conn) {
		this.connection = conn;
	}
}