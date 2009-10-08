package com.infosys.setlabs.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract JDBC Data Access Object.
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public abstract class JdbcDAO {
	// JDBC connection to the database
	private Connection connection;

	/**
	 * Creates a new DAO.
	 * 
	 * @param conn
	 *            connection to use
	 */
	public JdbcDAO(Connection conn) {
		this.setConnection(conn);
	}

	/**
	 * Closes a result set.
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
	 * Closes a prepared statement.
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
	 * Closes a statement.
	 * 
	 * @param statement
	 *            statement to close
	 */
	protected void closeStatement(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				System.out.println("SQLException: " + e.getMessage());
			}
		}
	}	

	/**
	 * Gets the connection.
	 * 
	 * @return connection
	 */
	protected Connection getConnection() {
		return this.connection;
	}

	/**
	 * Sets the connection.
	 * 
	 * @param conn
	 *            connection to set
	 */
	protected void setConnection(Connection conn) {
		this.connection = conn;
	}
}