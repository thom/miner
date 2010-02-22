package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerFileMovesDAO;

public class MysqlMinerFileMovesDAO extends JdbcDAO implements
		MinerFileMovesDAO {
	public static String tableName = "miner_actions";

	/**
	 * Creates a new DAO
	 * 
	 * @param conn
	 *            connection to connect to
	 */
	public MysqlMinerFileMovesDAO(Connection conn) {
		super(conn);
	}

	protected String createTableSQL() {
		return String.format("CREATE TABLE %s LIKE actions;", tableName);
	}

	protected String initializeTableSQL() {
		return String.format("INSERT INTO %s SELECT * FROM actions", tableName);
	}

	protected String dropTableSQL() {
		return String.format("DROP TABLE IF EXISTS %s", tableName);
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
		PreparedStatement ps = null;
		try {
			ps = this.getConnection().prepareStatement(dropTableSQL());
			ps.executeUpdate();
			ps.executeUpdate(createTableSQL());
			ps.executeUpdate(initializeTableSQL());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeStatement(ps);
		}
	}

}
