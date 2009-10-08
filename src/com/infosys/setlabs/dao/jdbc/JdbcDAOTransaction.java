package com.infosys.setlabs.dao.jdbc;

import java.sql.SQLException;

import com.infosys.setlabs.dao.DAOSession;
import com.infosys.setlabs.dao.DAOTransaction;
import com.infosys.setlabs.dao.DataAccessException;

public class JdbcDAOTransaction implements DAOTransaction {
	private JdbcDAOSession session;

	public JdbcDAOTransaction(JdbcDAOSession session) {
		this.session = session;
		try {
			session.getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void begin() throws DataAccessException {
	}

	public void commit() throws DataAccessException {
		try {
			this.session.getConnection().commit();
		} catch (SQLException se) {
			throw new DataAccessException(se);
		}
	}

	public void abort() throws DataAccessException {
		try {
			this.session.getConnection().rollback();
		} catch (SQLException se) {
			throw new DataAccessException(se);
		}
	}

	public DAOSession getSession() throws DataAccessException {
		return this.session;
	}
}