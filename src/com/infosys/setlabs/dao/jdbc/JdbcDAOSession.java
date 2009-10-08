package com.infosys.setlabs.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DAOSession;
import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;

/**
 * JDBC session
 * 
 * @author Thomas Weibel <thomas_401709@infosys.com>
 */
public class JdbcDAOSession implements DAOSession {
	// Connection
	private Connection conn;

	/**
	 * Creates a new JDBC session.
	 * 
	 * @param conn
	 *            connection to use
	 */
	public JdbcDAOSession(Connection conn) {
		this.conn = conn;
	}

	/**
	 * Gets a new transaction.
	 */
	public DAOTransaction getTransaction() {
		return new JdbcDAOTransaction(this);
	}

	/**
	 * Closes the session.
	 */
	public void close() throws DataAccessException {
		try {
			this.conn.close();
		} catch (SQLException se) {
			throw new DataAccessException(se);
		}
	}

	/**
	 * Gets the connection.
	 * 
	 * @return connection
	 */
	public Connection getConnection() {
		return this.conn;
	}
}