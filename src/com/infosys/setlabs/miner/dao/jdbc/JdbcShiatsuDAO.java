package com.infosys.setlabs.miner.dao.jdbc;

import java.sql.Connection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.ShiatsuDAO;

public abstract class JdbcShiatsuDAO extends JdbcDAO implements ShiatsuDAO {

	protected static String CREATE_MINER_FILES_TABLE = "" + "";
	protected static String CREATE_MINER_MODULES_TABLE = "" + "";

	public JdbcShiatsuDAO(Connection conn) {
		super(conn);
	}

	@Override
	public void massage() throws DataAccessException {
		createTables();
		initializeTables();
	}

	private void createTables() throws DataAccessException {
		// TODO
	}

	private void initializeTables() throws DataAccessException {
		// TODO
	}
}
