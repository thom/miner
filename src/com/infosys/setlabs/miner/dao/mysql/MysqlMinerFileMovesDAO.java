package com.infosys.setlabs.miner.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.infosys.setlabs.dao.DataAccessException;
import com.infosys.setlabs.dao.jdbc.JdbcDAO;
import com.infosys.setlabs.miner.dao.MinerFileMovesDAO;
import com.infosys.setlabs.miner.domain.MinerFile;

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

	protected String findMovesSQL() {
		return String.format("SELECT f1.file_name, a1.id AS d_action_id, "
				+ "a2.id AS a_action_id, a1.file_id AS old_file_id, "
				+ "a2.file_id AS new_file_id, a1.commit_id "
				+ "FROM %s a1, %s a2, files f1, files f2 "
				+ "WHERE f1.id = a1.file_id AND f2.id = a2.file_id "
				+ "AND f1.file_name = f2.file_name AND a1.type = 'D' "
				+ "AND a2.type = 'A' AND a1.commit_id = a2.commit_id "
				+ "ORDER BY a1.commit_id", tableName, tableName);
	}

	@Override
	public void initialize() throws DataAccessException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// Find moves (D-A pattern with same commit ID)			
			ps = this.getConnection().prepareStatement(findMovesSQL());
			rs = ps.executeQuery();
			int i = 1;
			while (rs.next()) {
				System.out.println(i + ": " + rs.getString("file_name"));
				i++;
				// TODO: Modify action "A" to "MINER_MOVE"
				// TODO: Deleted action "D"
				// TODO: Replace all occurences of old_file_id with new_file_id in
				// miner_actions table
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.closeResultSet(rs);
			this.closeStatement(ps);
		}
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
