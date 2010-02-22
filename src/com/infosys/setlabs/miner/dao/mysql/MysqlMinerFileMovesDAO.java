package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerFileMovesDAO;

public class MysqlMinerFileMovesDAO extends JdbcDAO implements
		MinerFileMovesDAO {

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlMinerFileMovesDAO(Connection conn) {
		super(conn);
	}

	@Override
	public void initialize() throws DataAccessException {
		// TODO: Find moves (D-A pattern)
		// TODO: Delete actions leading to the move (delete and add)
		// TODO: Add new action "MINER_MOVE" with new file ID and action ID
		// of the delete action
		// TODO: Replace all uses of actions with miner actions
	}

	@Override
	public void createTables() throws DataAccessException {
		// TODO: Create miner actions table
	}

}
