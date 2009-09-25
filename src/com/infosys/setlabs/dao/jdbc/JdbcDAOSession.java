package com.infosys.setlabs.dao.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DAOSession;
import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;

public class JdbcDAOSession implements DAOSession {
	private Connection conn;

	public JdbcDAOSession(Connection conn) {
		this.conn = conn;
	}

	public DAOTransaction getTransaction() {
		return new JdbcDAOTransaction(this);
	}

	public void close() throws DataAccessException {
		try {
			this.conn.close();
		} catch (SQLException se) {
			throw new DataAccessException(se);
		}
	}

	public Connection getConnection() {
		return this.conn;
	}
}