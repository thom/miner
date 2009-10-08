package com.infosys.setlabs.dao.jdbc;

import java.sql.SQLException;

import com.infosys.setlabs.dao.DAOSession;
import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;

public class JdbcDAOTransaction implements DAOTransaction {
	private JdbcDAOSession session;

	/**
	 * Disables auto-commit.
	 * 
	 * When a connection is created, it is in auto-commit mode. This means that
	 * each individual SQL statement is treated as a transaction and is
	 * automatically committed right after it is executed.
	 * 
	 * The way to allow two or more statements to be grouped into a transaction
	 * is to disable auto-commit mode.
	 * 
	 * Once auto-commit mode is disabled, no SQL statements are committed until
	 * one calls the method <code>commit</code> explicitly. All statements
	 * executed after the previous call to the method <code>commit</code> are
	 * included in the current transaction and committed together as a unit.
	 * 
	 * @param session
	 */
	public JdbcDAOTransaction(JdbcDAOSession session) {
		this.session = session;

		try {
			session.getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Begins a new transaction.
	 */
	public void begin() throws DataAccessException {
	}

	/**
	 * Commits a transaction.
	 */
	public void commit() throws DataAccessException {
		try {
			this.session.getConnection().commit();
		} catch (SQLException se) {
			throw new DataAccessException(se);
		}
	}

	/**
	 * Aborts a transaction.
	 */
	public void abort() throws DataAccessException {
		try {
			this.session.getConnection().rollback();
		} catch (SQLException se) {
			throw new DataAccessException(se);
		}
	}

	/**
	 * Returns the session.
	 */
	public DAOSession getSession() throws DataAccessException {
		return this.session;
	}
}